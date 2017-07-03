package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;

import java.util.List;

/**
 * Created by nyq on 2017/7/3.
 */
public interface CommonLogMapper {
    List<LogGroup> listLogGroupsPage(@Param("userId")String userId,
                                     @Param("state") int state,
                                     @Param("isShared") int isShared,
                                     @Param("keyWord") String keyWord,
                                     @Param("offset") int offset,
                                     @Param("size") int size);

    Integer countLogs(@Param("userId")String userId,
                      @Param("state") int state,
                      @Param("isShared") int isShared,
                      @Param("keyWord") String keyWord);

    Integer lineOfLogId(@Param("id") String id,
                        @Param("userId")String userId,
                        @Param("state") int state,
                        @Param("isShared") int isShared);
}
