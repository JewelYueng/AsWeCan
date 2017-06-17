package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
public interface MergeMethodService {
    MergeMethod getMethodById(String id);
    List<MergeMethod> getActiveMethods();
    List<MergeMethod> getAllMethods();
    Map<String, Object> getMethodConfig(MergeMethod mergeMethod);
    Map<String, Object> getMethodConfig(String methodId);
    boolean isActive(String id);
    boolean isActive(MergeMethod mergeMethod);
    EventLog merge(EventLog eventLog1, EventLog eventLog2, String methodId, Map<String, Object> params);
}
