package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface EventLogService {
    EventLog getEventLogById(String id);
    boolean save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize);
    boolean saveInDB(EventLog log);
    List<LogGroup> getLogsByUserId(String userId);
    List<LogGroup> getSharedLogs();
    List<LogGroup> getLogByFuzzyName(String keyWord,User user);
    int updateShareStateByLogId(List<String> idList,int isShared, String userId);
    int updateStateByLogId(List<String> idList,int state, String userId);

    void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId);
    void updateStateByLogIdForUser(List<String> ids, int state, String userId);
}
