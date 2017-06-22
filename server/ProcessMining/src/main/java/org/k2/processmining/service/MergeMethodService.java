package org.k2.processmining.service;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.impl.MergeMethodServiceImpl;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    MergeMethodServiceImpl.MergeResult merge(EventLog eventLog1, EventLog eventLog2, String methodId, Map<String, Object> params);
    void afterSaveInLogStorage(EventLog resultEventLog, XLog resultXLog);
    MergeMethod addMethod(MultipartFile[] multipartFiles) throws IOException, LoadMethodException;
    void afterSaveMethod(MergeMethod mergeMethod);
    void setMethodState(List<String> ids, int state);

    @Transactional
    void delete(List<String> ids);
}
