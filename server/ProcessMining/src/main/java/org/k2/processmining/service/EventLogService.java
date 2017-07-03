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
public interface EventLogService extends CommonLogService<EventLog> {
    void save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize) throws IOException;
    List<EventLog> getEventLogsByIds(List<String> ids);
}
