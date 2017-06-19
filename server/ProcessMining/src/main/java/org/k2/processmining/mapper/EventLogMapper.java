package org.k2.processmining.mapper;

import org.k2.processmining.model.log.EventLog;
import org.springframework.stereotype.Repository;

/**
 * Created by nyq on 2017/6/19.
 */
@Repository
public interface EventLogMapper {
    void save(EventLog eventLog);
}
