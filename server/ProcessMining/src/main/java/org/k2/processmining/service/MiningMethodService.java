package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    MiningMethod addMethod(MultipartFile[] multipartFiles) throws IOException, LoadMethodException;
    void afterSaveMethod(MiningMethod miningMethod);
    void setMethodState(List<String> ids, int state);

    @Transactional
    void delete(List<String> ids);
}
