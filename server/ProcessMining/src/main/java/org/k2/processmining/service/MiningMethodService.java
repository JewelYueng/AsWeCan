package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
public interface MiningMethodService {
    MiningMethod getMethodById(String id);
    List<MiningMethod> getActiveMethods();
    List<MiningMethod> getAllMethods();
    Map<String, Object> getMethodConfig(MiningMethod miningMethod);
    Map<String, Object> getMethodConfig(String methodId);
    boolean isActive(String id);
    boolean isActive(MiningMethod miningMethod);
    String mining(String userId, String eventId, String methodId, Map<String, Object> params);
    String mining(EventLog eventLog, String methodId, Map<String, Object> params);
}
