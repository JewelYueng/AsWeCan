package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/19.
 */
@Repository
public interface NormalLogMapper {

    List<LogGroup> listLogGroupsByUserIdAndState(@Param("userId") String userId, @Param("state") int state);
    List<LogGroup> listLogGroupsByStateAndSharedState(@Param("state")int state, @Param("isShared") int isShared);
    List<LogGroup> listLogGroupsByFuzzyName(String keyWord);

    NormalLog getNormalLogById(@Param("id") String id);


//    void updateShareStateByLogId(List<NormalLog> normalLogList);

    void updateShareStateByLogId(NormalLog normalLog);
    void save(NormalLog normalLog);
    void updateLogStateByLogId(NormalLog normalLog);
}
