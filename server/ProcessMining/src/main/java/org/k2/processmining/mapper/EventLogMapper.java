package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.log.EventLog;
import org.springframework.stereotype.Repository;

/**
 * Created by nyq on 2017/6/19.
 */
@Repository
public interface EventLogMapper {
    EventLog getEventLogById(@Param("id") String id);
    void save(EventLog eventLog);
}
