package org.k2.processmining.service;

import org.k2.processmining.model.miningmethod.MiningMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
public interface MiningMethodService {
    List<MiningMethod> getActiveMethods();
    List<MiningMethod> getAllMethods();
    Map<String, Object> getMethodConfig(MiningMethod miningMethod);
    Map<String, Object> getMethodConfig(String methodId);
    boolean isActive(String id);
    String mining(String userId, String eventId, String methodId);
}
