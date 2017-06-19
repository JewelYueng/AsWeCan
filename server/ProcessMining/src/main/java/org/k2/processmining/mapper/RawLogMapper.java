package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.RawLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nyq on 2017/6/18.
 */
@Repository
public interface RawLogMapper {
    void save(RawLog log);
    List<LogGroup> listLogsByUserIdAndState(@Param("id") String id, @Param("state") int state);
    List<LogGroup> listLogsByState(@Param("state") int state);
}
