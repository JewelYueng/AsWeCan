package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.support.event.transform.TransToEvent;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/17.
 */

@Service
public class NormalLogServiceImpl implements NormalLogService {

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private NormalLogMapper normalLogMapper;

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    private EventLogParse eventLogParse;

    @Override
    public EventLog transToEventLog(NormalLog normalLog) {
        EventLog eventLog = new EventLog();
        eventLog.setUserId(normalLog.getUserId());
        eventLog.setId(Util.getUUIDString());
        eventLog.setNormalLogId(normalLog.getId());
        eventLog.setCreateDate(new Date());
        eventLog.setFormat("xes");
        eventLog.setLogName(Util.getTransEventName(normalLog.getLogName()));
        String tmpdir = System.getProperty("java.io.tmpdir");
        String name = eventLog.getId();
        File file = logStorage.download(normalLog, inputStream -> TransToEvent.transToEvent(inputStream, tmpdir, name));
        if (file == null || !file.isFile()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            if (logStorage.upload(eventLog, inputStream)) {
                try ( InputStream inputForParse = new FileInputStream(file)){
                    XLog xLog = eventLogParse.eventLogParse(inputForParse);
                    if (xLog == null) {
                        logStorage.delete(eventLog);
                        return null;
                    }
                    EventLogSummary eventLogSummary = Summarize.summarizeXLog(xLog);
                    eventLog.setCaseNumber(eventLogSummary.getCases());
                    eventLog.setEventNames(eventLogSummary.getEventNames());
                    eventLog.setEventNumber(eventLogSummary.getEvents());
                    eventLog.setOperatorNames(eventLogSummary.getOperatorNames());
                    eventLogMapper.save(eventLog);
                }
                return eventLog;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户的规范化日志列表
     * @param userId
     * @return
     */
    @Override
    public List<LogGroup> getLogGroupsByUserId(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<LogGroup> logGroups = normalLogMapper.listLogGroupsByUserIdAndState(userId, LogState.ACTIVE.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (!Util.isActive(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (!Util.isActive(logGroup.getEventLog())) {
                logGroup.setEventLog(null);
            }
            if (logGroup.getRawLog() == null && logGroup.getNormalLog() == null && logGroup.getEventLog() == null) {
                iterator.remove();
            }
        }
        return logGroups;
    }


    /**
     * 获取分享的规范化日志列表
     * @return
     */
    @Override
    public List<LogGroup> getSharedLogGroups() {
        List<LogGroup> logGroups = normalLogMapper.listLogGroupsByStateAndSharedState(LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (! Util.isActiveAndShared(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (! Util.isActiveAndShared(logGroup.getEventLog())) {
                logGroup.setEventLog(null);
            }
            if (logGroup.getRawLog() == null && logGroup.getNormalLog() == null && logGroup.getEventLog() == null) {
                iterator.remove();
            }
        }
        return logGroups;
    }

    @Override
    public NormalLog getNormalLogById(String id) {
        return normalLogMapper.getNormalLogById(id);
    }

    @Override
    public int updateShareStateByLogId(List<String> idList,int isshared) {

        for (String id : idList) {
            NormalLog normalLog = new NormalLog();
            normalLog.setId(id);
            normalLog.setState(isshared);//分享
            normalLogMapper.updateShareStateByLogId(normalLog);
        }
        return 1;
    }

    @Override
    public int deleteLogByLogId(List<String> idList) {
        return 0;
    }

    @Override
    public void addNormalLog(NormalLog normalLog) {
        System.out.println("id:"+normalLog.getId());
        normalLogMapper.save(normalLog);
    }

    @Override
    public boolean save(NormalLog normalLog, InputStream inputStream) {
        if (logStorage.upload(normalLog, inputStream)) {
            normalLogMapper.save(normalLog);
            return true;
        }
        return false;
    }
}
