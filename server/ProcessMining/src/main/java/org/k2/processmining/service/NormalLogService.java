package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService {

    EventLog transToEventLog(NormalLog normalLog);
}
