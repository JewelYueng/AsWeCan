package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.MiningMethodMapper;
import org.k2.processmining.model.State;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MinerFactory;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.mining.Miner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class MiningMethodServiceImpl implements MiningMethodService {

    @Autowired
    private MiningMethodMapper miningMethodMapper;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogParse eventLogParse;

    @Override
    public List<MiningMethod> getActiveMethods() {
        return miningMethodMapper.listMethodByState(State.ACTIVE.getValue());
    }

    @Override
    public List<MiningMethod> getAllMethods() {
        return miningMethodMapper.listMethods();
    }

    public Map<String, Object> getMethodConfig(MiningMethod miningMethod) {
        return miningMethod == null ? new HashMap<>() : getMethodConfig(miningMethod.getId());
    }

    public Map<String, Object> getMethodConfig(String methodId) {
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null) {
            return new HashMap<>();
        }
        return algorithm.getConfigMap();
    }

    @Override
    public boolean isActive(String id) {
        MiningMethod miningMethod = miningMethodMapper.getMethodById(id);
        return miningMethod != null && State.ACTIVE.getValue() == miningMethod.getState();
    }

    @Override
    public String mining(String userId, String eventId, String methodId) {
        EventLog eventLog = new EventLog();
        eventLog.setUserId(userId);
        eventLog.setId(eventId);
        return mining(eventLog, methodId);
    }

    public String mining(EventLog eventLog, String methodId) {
        XLog xLog = logStorage.download(eventLog, inputStream -> eventLogParse.eventLogParse(inputStream));
        Algorithm<Miner> algorithm = MinerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm != null && algorithm.getAlgorithm() != null) {
            return algorithm.getAlgorithm().mining(xLog, new HashMap<>());
        }
        System.out.println("fail to mining");
        return "";
    }
}
