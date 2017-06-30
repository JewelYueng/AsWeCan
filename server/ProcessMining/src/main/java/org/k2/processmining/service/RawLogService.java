package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<LogGroup> getLogGroups();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<LogGroup> getLogGroupsByKeyWord(String keyWord);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteByAdmin(List<String> ids);

    RawLog getRawLogById(String id);
    List<LogGroup> getLogsByUser(User user);
    List<LogGroup> getSharedLogs();
    List<LogGroup> getLogByFuzzyName(String keyWord,User user);
    List<LogGroup> getSharedLogsByFuzzyName(String keyWord);
    boolean save(RawLog log, InputStream inputStream);

    @PreAuthorize("hasPermission(#rawLog, 'normalize')")
    NormalLog normalize(RawLog rawLog, LogConfiguration lc);
    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);
    void afterSaveInLogStorage(RawLog log);

    @Transactional
    void afterSaveInLogStorageForNormalize(NormalLog normalLog, RawLog rawLog);
}
