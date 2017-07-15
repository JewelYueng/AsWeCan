package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/19.
 */
@Repository
public interface NormalLogMapper extends CommonLogMapper<NormalLog> {
    List<LogGroup> listLogGroups(@Param("userId")String userId,
                                 @Param("state") int state,
                                 @Param("isShared") int isShared,
                                 @Param("keyWord") String keyWord);

    @Update("UPDATE normal_log SET state=2 WHERE raw_log_id=#{rawLogId}")
    void deleteNormalLogByRawLogId(@Param("rawLogId") String rawLogId);
}
