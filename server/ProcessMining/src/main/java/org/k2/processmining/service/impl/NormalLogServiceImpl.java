package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
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
    private EventLogService eventLogService;

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
                    eventLogService.saveInDB(eventLog);
                }
                return eventLog;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("io");
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
        List<LogGroup> logGroups = normalLogMapper.listLogGroups(userId, LogState.ACTIVE.getValue(),-1, null);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }


    /**
     * 获取分享的规范化日志列表
     * @return
     */
    @Override
    public List<LogGroup> getSharedLogGroups() {
        List<LogGroup> logGroups = normalLogMapper.listLogGroups(null, LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue(), null);
        verifyLogGroupsIsShared(logGroups);
        return logGroups ;
    }

    /**
     * 通过关键字获取日志
     * @param keyWord
     * @return
     */
    @Override
    public List<LogGroup> getLogByFuzzyName(String keyWord, User user) {
        List<LogGroup> logGroups = normalLogMapper.listLogGroups(user.getId(), LogState.ACTIVE.getValue(), -1, keyWord);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    @Override
    public NormalLog getNormalLogById(String id) {
        return normalLogMapper.getNormalLogById(id);
    }

    @Override
    public boolean save(NormalLog normalLog, InputStream inputStream) {
        if (logStorage.upload(normalLog, inputStream)) {
            return saveInDB(normalLog);
        }
        return false;
    }

    @Override
    public boolean saveInDB(NormalLog normalLog) {
        normalLogMapper.save(normalLog);
        return true;
    }

    @Override
    public void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId) {
        normalLogMapper.updateIsShared(ids, isShared, userId);
    }

    @Override
    public void updateStateByLogIdForUser(List<String> ids, int state, String userId) {
        normalLogMapper.updateLogState(ids, state, userId);
    }

    private void verifyLogGroupsIsActive(List<LogGroup> logGroups){
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()){
            LogGroup logGroup = iterator.next();
            if (logGroup.getNormalLog() == null){
                iterator.remove();
                continue;
            }
            if (!Util.isActive(logGroup.getRawLog())){
                logGroup.setRawLog(null);
            }
            if (!Util.isActive(logGroup.getEventLog())){
                logGroup.setEventLog(null);
            }
        }
    }

    private void verifyLogGroupsIsShared(List<LogGroup> logGroups) {
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (logGroup.getNormalLog() == null) {
                iterator.remove();
                continue;
            }
            if (! Util.isActiveAndShared(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (! Util.isActiveAndShared(logGroup.getEventLog())) {
                logGroup.setEventLog(null);
            }
        }
    }
}
