package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/18.
 */
@Repository
public interface RawLogMapper extends CommonLogMapper<RawLog> {

    // userId is null : get all user
    // userId is not null : where userid=userId
    // state
    //isShared :  if isShared=-1, don't treat as a condition, which means get all logs, including shared and unShared
    // keyWord
    List<LogGroup> listLogGroups(@Param("userId")String userId,
                            @Param("state") int state,
                            @Param("isShared") int isShared,
                            @Param("keyWord") String keyWord);
}
