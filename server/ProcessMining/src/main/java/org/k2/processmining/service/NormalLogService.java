package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService extends CommonLogService<NormalLog> {
    @Transactional
    EventLog transToEventLog(NormalLog normalLog);
}
