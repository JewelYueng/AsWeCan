package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/19.
 */
@Service
public class EventLogServiceImpl implements EventLogService {

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogParse eventLogParse;

    @Override
    public EventLog getEventLogById(String id) {
        return eventLogMapper.getEventLogById(id);
    }

    @Override
    public List<LogGroup> getLogsByUserId(String userId) {
        List<LogGroup> logGroups = eventLogMapper.listLogGroups(userId, LogState.ACTIVE.getValue(), -1, null);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogs() {
        List<LogGroup> logGroups = eventLogMapper.listLogGroups(null, LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue(), null);
        verifyLogGroupsIsShared(logGroups);
        return logGroups;
    }

    /**
     * 根据关键字搜索日志
     * @param keyWord
     * @param user
     * @return
     */
    @Override
    public List<LogGroup> getLogByFuzzyName(String keyWord, User user) {
        List<LogGroup> logGroups = eventLogMapper.listLogGroups(user.getId(), LogState.ACTIVE.getValue(), -1, keyWord);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    /**
     * 更新日志的分享状态
     * @param idList
     * @param isShared
     * @param userId
     * @return
     */
    @Override
    public int updateShareStateByLogId(List<String> idList,int isShared, String userId) {
        eventLogMapper.updateIsShared(idList, isShared, userId);
        return 1;
    }

    /**
     * 更新日志的状态
     * @param idList
     * @param state
     * @return
     */
    @Override
    public int updateStateByLogId(List<String> idList, int state, String userId) {
        eventLogMapper.updateLogState(idList, state, userId);
        return 1;
    }

    @Override
    public void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId) {
        eventLogMapper.updateIsShared(ids, isShared, userId);
    }

    @Override
    public void updateStateByLogIdForUser(List<String> ids, int state, String userId) {
        eventLogMapper.updateLogState(ids, state, userId);
    }

    @Override
    public boolean save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize) {

        if (! logStorage.upload(log, inputForRemote)) {
            return false;
        }
        XLog xLog = eventLogParse.eventLogParse(inputForSummarize);
        if (xLog == null) {
            logStorage.delete(log);
            return false;
        }
        EventLogSummary eventLogSummary = Summarize.summarizeXLog(xLog);
        log.setCaseNumber(eventLogSummary.getCases());
        log.setEventNames(eventLogSummary.getEventNames());
        log.setEventNumber(eventLogSummary.getEvents());
        log.setOperatorNames(eventLogSummary.getOperatorNames());
        return saveInDB(log);
    }

    @Override
    public boolean saveInDB(EventLog log) {
        eventLogMapper.save(log);
        return true;
    }

    private void verifyLogGroupsIsActive(List<LogGroup> logGroups) {
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (logGroup.getEventLog() == null) {
                iterator.remove();
                continue;
            }
            if (!Util.isActive(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (!Util.isActive(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
            }
        }
    }

    private void verifyLogGroupsIsShared(List<LogGroup> logGroups) {
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (logGroup.getEventLog() == null) {
                iterator.remove();
                continue;
            }
            if (! Util.isActiveAndShared(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (! Util.isActiveAndShared(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
            }
        }
    }
}
