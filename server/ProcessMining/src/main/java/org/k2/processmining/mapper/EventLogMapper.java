package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.RawLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/19.
 */
@Repository
public interface EventLogMapper {
    EventLog getEventLogById(@Param("id") String id);
    void save(EventLog eventLog);
    List<LogGroup> listLogsByUserIdAndState(@Param("userId") String userId, @Param("state") int state);
    List<LogGroup> listLogsByStateAndSharedState(@Param("state") int state, @Param("isShared") int isShared);
    List<LogGroup> listLogsByFuzzyName(Map request);
    void updateShareStateByLogId(EventLog eventLog);
    void updateLogStateByLogId(EventLog eventLog);
}
