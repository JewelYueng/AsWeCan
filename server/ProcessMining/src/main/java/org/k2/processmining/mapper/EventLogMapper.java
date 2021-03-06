package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
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
public interface EventLogMapper extends CommonLogMapper<EventLog> {
    List<LogGroup> listLogGroups(@Param("userId")String userId,
                                 @Param("state") int state,
                                 @Param("isShared") int isShared,
                                 @Param("keyWord") String keyWord);

    @Update("UPDATE event_log SET state=2 WHERE normal_log_id=#{normalLogId}")
    void deleteEventLogByNormalLogId(@Param("normalLogId") String normalLogId);


    List<EventLog> listEventLogByIds(@Param("ids") List<String> ids);
}
