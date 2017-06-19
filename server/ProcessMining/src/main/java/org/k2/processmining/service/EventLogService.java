package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;

/**
 * Created by Aria on 2017/6/13.
 */
public interface EventLogService {
    EventLog getEventLogById(String id);
}
