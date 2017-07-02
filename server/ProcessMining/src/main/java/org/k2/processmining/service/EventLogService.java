package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface EventLogService {
    EventLog getEventLogById(String id);
    void save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize) throws IOException;
    void save(EventLog log);
    List<LogGroup> getLogsByUserId(String userId);
    List<LogGroup> getSharedLogs();
    List<LogGroup> getLogByFuzzyName(String keyWord,User user);
    List<LogGroup> getSharedLogsByFuzzyName(String keyWord);
    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);
}
