package org.k2.processmining.service;

import cern.jet.random.Normal;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService {

    List<LogGroup> getLogGroupsByUserId(String userId);
    List<LogGroup> getSharedLogGroups();
    List<LogGroup> getLogByFuzzyName(String keyWord, User user);
    List<LogGroup> getSharedLogsByFuzzyName(String keyWord);
    NormalLog getNormalLogById(String id);
    void save(NormalLog normalLog, InputStream inputStream) throws IOException;
    void save(NormalLog normalLog);

    @PreAuthorize("hasPermission(#normalLog, 'transToEvent')")
    @Transactional
    EventLog transToEventLog(NormalLog normalLog);
    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);

//    @Transactional
//    void afterSaveInLogStorageForTransToEventLog(EventLog eventLog, NormalLog normalLog, File file);
}
