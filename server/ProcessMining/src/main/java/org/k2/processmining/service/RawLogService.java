package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.normal.transform.LogConfiguration;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService {
    RawLog getRawLogById(String id);
    List<LogGroup> getLogsByUser(User user);
    List<LogGroup> getSharedLogs();
    boolean save(RawLog log, InputStream inputStream);
    int updateShareStateByLogId(List<String> idList,int isshared);
    int updateStateByLogId(List<String> idList,int state);
    NormalLog normalize(RawLog rawLog, LogConfiguration lc);
}
