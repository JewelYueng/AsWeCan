package org.k2.processmining.mapper;

import org.k2.processmining.model.log.NormalLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/19.
 */
@Repository
public interface NormalLogMapper {

    List<NormalLog> getNormalLogsByUserId(String userId);
    List<NormalLog> getAllSharedNormalLogs();

//    void updateShareStateByLogId(List<NormalLog> normalLogList);

    void updateShareStateByLogId(NormalLog normalLog);
    void save(NormalLog normalLog);
}
