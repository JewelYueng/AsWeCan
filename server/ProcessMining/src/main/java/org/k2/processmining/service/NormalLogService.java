package org.k2.processmining.service;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService {

    List<NormalLog> getNormalLogsByUserId(String userId);

    List<NormalLog> getAllSharedNormalLogs();

    int updateShareStateByLogId(List<NormalLog> normalLogList,int isshared);

    void addNormalLog(NormalLog normalLog);

    EventLog transToEventLog(NormalLog normalLog);
}
