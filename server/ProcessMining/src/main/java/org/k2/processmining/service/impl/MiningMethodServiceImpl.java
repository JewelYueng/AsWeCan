package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.MiningMethodMapper;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.support.algorithm.MethodManage;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.reflect.ReflectUtil;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<MiningMethod> getActiveMethods() {
        return miningMethodMapper.listMethodByState(LogState.ACTIVE.getValue());
    }

    @Override
    public List<MiningMethod> getAllMethods() {
        return miningMethodMapper.listMethods();
    }

    public Map<String, Object> getMethodConfig(MiningMethod miningMethod) {
        return miningMethod == null ? Collections.emptyMap() : getMethodConfig(miningMethod.getId());
    }

    public Map<String, Object> getMethodConfig(String methodId) {
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null) {
            return Collections.emptyMap();
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
    public MiningMethod addMethod(MultipartFile[] multipartFiles) throws IOException, LoadMethodException {
        MiningMethod miningMethod = new MiningMethod();
        miningMethod.setId(Util.getUUIDString());

        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()){
                methodManage.saveMinerJar(miningMethod.getId(), file.getOriginalFilename(), inputStream);
            }
        }
        Algorithm<Miner> minerAlgorithm = methodManage.loadMinerById(miningMethod.getId());
        miningMethod.setMethodName((String)minerAlgorithm.getConfigMap().get("key"));
        MinerFactory.getInstance().put(miningMethod.getId(), minerAlgorithm);
        miningMethodService.afterSaveMethod(miningMethod);
        return miningMethod;
    }

    @Override
    public void afterSaveMethod(MiningMethod miningMethod) {
        miningMethodMapper.save(miningMethod);
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
    public String mining(String userId, String eventId, String methodId, Map<String, Object> params) {
        EventLog eventLog = new EventLog();
        eventLog.setUserId(userId);
        eventLog.setId(eventId);
        return mining(eventLog, methodId, params);
    }

    public String mining(EventLog eventLog, String methodId, Map<String, Object> params) {
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null || algorithm.getAlgorithm() == null) {
            return "";
        }
        XLog xLog = logStorage.download(eventLog, inputStream -> eventLogParse.eventLogParse(inputStream));
        if (xLog != null) {
            return algorithm.getAlgorithm().mining(xLog, params).toString();
        }
        return "";
    }
}
