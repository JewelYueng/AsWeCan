package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService {
    RawLog getRawLogById(String id);
    List<LogGroup> getLogsByUser(User user);
    List<LogGroup> getSharedLogs();
    List<LogGroup> getLogByFuzzyName(String keyWord,User user);
    boolean save(RawLog log, InputStream inputStream);
    NormalLog normalize(RawLog rawLog, LogConfiguration lc);
    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);
    void afterSaveInLogStorage(RawLog log);

    @Transactional
    void afterSaveInLogStorageForNormalize(NormalLog normalLog, RawLog rawLog);
}
