package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.cache.CacheConfig;
import org.k2.processmining.exception.JSONInternalServerErrorException;
import org.k2.processmining.mapper.MiningMethodMapper;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.support.algorithm.MethodManage;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.DiagramType;
import org.k2.processmining.support.reflect.ReflectUtil;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class MiningMethodServiceImpl implements MiningMethodService {

    @Autowired
    private MiningMethodService miningMethodService;

    @Autowired
    private MiningMethodMapper miningMethodMapper;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogParse eventLogParse;

    @Autowired
    private MethodManage methodManage;

    public MiningMethod getMethodById(String id) {
        return miningMethodMapper.getMethodById(id);
    }

    @Override
    public Algorithm<Miner> getAlgorithmById(String id) {
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(id);
        if (algorithm == null) {
            return null;
        }
        MiningMethod miningMethod = getMethodById(id);
        if (miningMethod == null || !MethodState.isActive(miningMethod.getState())) {
            return null;
        }
        return algorithm;
    }

    @Override
    public List<MiningMethod> getActiveMethods() {
        return miningMethodMapper.listMethodByState(LogState.ACTIVE.getValue());
    }

    @Override
    public List<MiningMethod> getAllMethods() {
        return miningMethodMapper.listMethods();
    }

    public Map<String, Object> getMethodConfig(MiningMethod miningMethod) {
        return miningMethod == null ? null : getMethodConfig(miningMethod.getId());
    }

    public Map<String, Object> getMethodConfig(String methodId) {
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null) {
            return null;
        }
        return algorithm.getConfigMap();
    }

    @Override
    public boolean isActive(String id) {
        MiningMethod miningMethod = getMethodById(id);
        return miningMethod != null && MethodState.isActive(miningMethod.getState()) ;
    }

    public boolean isActive(MiningMethod miningMethod) {
        return miningMethod != null && isActive(miningMethod.getId());
    }

    @Override
    public MiningMethod addMethod(MiningMethod miningMethod, MultipartFile[] multipartFiles) throws IOException, LoadMethodException {
        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()){
                methodManage.saveMinerJar(miningMethod.getId(), file.getOriginalFilename(), inputStream);
            }
        }
        Algorithm<Miner> minerAlgorithm = methodManage.loadMinerById(miningMethod.getId());
        miningMethod.setMethodName((String)minerAlgorithm.getConfigMap().get("key"));
        MinerFactory.getInstance().put(miningMethod.getId(), minerAlgorithm);
        miningMethodMapper.save(miningMethod);
        return miningMethod;
    }

    @Override
    public void setMethodState(List<String> ids, int state) {
        miningMethodMapper.updateState(ids, state);
    }

    @Override
    public void delete(List<String> ids) {
        miningMethodMapper.delete(ids);
        for (String id : ids) {
            MinerFactory.getInstance().deleteAlgorithm(id);
            ReflectUtil.getInstance().closeClassLoader(id);
        }
        System.gc();
//        for (String id :ids) {
//            methodManage.deleteMerger(id);
//        }
    }

    @Override
    public TimeResult mining(EventLog eventLog, Algorithm<Miner> algorithm, Map<String,Object> params, DiagramType type) {
        XLog xLog = eventLogParse.eventLogParse(eventLog);
        if (xLog == null || algorithm == null) {
            throw new JSONInternalServerErrorException();
        }
        TimeResult<Object> timeResult = new TimeResult<>();
        TimeResult<SimpleHeuristicsNet> netResult = miningMethodService.mining(algorithm, eventLog, xLog, params);
        SimpleHeuristicsNet net = netResult.getResult();
        timeResult.setTime(netResult.getTime());
        Object res = null;
        Miner miner = algorithm.getAlgorithm();
        switch (type) {
            case PetriNet: default:
                res = miner.toPetriNet(net, xLog);
                break;
            case ResourceRelation:
                res = miner.toResourceRelation(net, xLog).values();
                break;
            case TransitionSystem:
                res = miner.toTransitionSystem(net, xLog);
                break;
            case Sankey:
                res = miner.toSankey(net, xLog);
                break;
        }
        timeResult.setResult(res);
        return timeResult;
    }

    @Override
    @Cacheable(value = CacheConfig.NET_CACHE, keyGenerator = "netKeyGenerator")
    public TimeResult<SimpleHeuristicsNet> mining(Algorithm<Miner> algorithm, EventLog eventLog, XLog xLog, Map<String,Object> params) {
        TimeResult<SimpleHeuristicsNet> timeResult = new TimeResult<>();
        timeResult.start();
        SimpleHeuristicsNet net = algorithm.getAlgorithm().mining(xLog, params);
        timeResult.stop();
        timeResult.setResult(net);
        return timeResult;
    }
}
