package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/18.
 */
@Repository
public interface RawLogMapper {
    RawLog getRawLogById(@Param("id") String id);
    void save(RawLog log);
    List<LogGroup> listLogsByUserIdAndState(@Param("userId") String userId, @Param("state") int state);
    List<LogGroup> listLogsByStateAndSharedState(@Param("state") int state, @Param("isShared") int isShared);
    List<LogGroup> listLogsByFuzzyName(Map request);
    void updateShareStateByLogId(RawLog rawLog);
    void updateLogStateByLogId(RawLog rawLog);
}
