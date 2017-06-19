package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService {

    List<LogGroup> getLogGroupsByUserId(String userId);

    List<LogGroup> getSharedLogGroups();

    NormalLog getNormalLogById(String id);

    int updateShareStateByLogId(List<String> idList,int isshared);

    int deleteLogByLogId(List<String> idList);

    void addNormalLog(NormalLog normalLog);

    boolean save(NormalLog normalLog, InputStream inputStream);

    EventLog transToEventLog(NormalLog normalLog);
}
