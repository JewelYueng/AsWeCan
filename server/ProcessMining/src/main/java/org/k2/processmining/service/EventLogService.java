package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface EventLogService {
    EventLog getEventLogById(String id);
    boolean save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize);
    List<LogGroup> getLogsByUserId(String userId);
    List<LogGroup> getSharedLogs();
}
