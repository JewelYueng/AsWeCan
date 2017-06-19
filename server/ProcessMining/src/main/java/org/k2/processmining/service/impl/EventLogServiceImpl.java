package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nyq on 2017/6/19.
 */
@Service
public class EventLogServiceImpl implements EventLogService {

    @Autowired
    private EventLogMapper eventLogMapper;

    @Override
    public EventLog getEventLogById(String id) {
        return eventLogMapper.getEventLogById(id);
    }
}
