package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.exception.BadRequestException;
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
import org.k2.processmining.support.event.parse.EventLogParseException;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogServiceImpl.class);

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

    @Override
    public List<LogGroup> getSharedLogsByFuzzyName(String keyWord) {
        List<LogGroup> logGroups = eventLogMapper.listLogGroups(null, LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue(), keyWord);
        verifyLogGroupsIsShared(logGroups);
        return logGroups;
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
    public void save(EventLog log, InputStream inputForRemote, InputStream inputForSummarize) throws IOException {
        logStorage.upload(log, inputForRemote);
        try {
            XLog xLog = eventLogParse.eventLogParse(inputForSummarize);
            Summarize.summarizeTo(xLog, log);
        }
        catch (EventLogParseException e) {
            LOGGER.error("Fail to parse eventLog:", e);
            throw new BadRequestException("Fail to parse eventLog. Please check the content of eventLog");
        }
        eventLogMapper.save(log);
    }

    @Override
    public void save(EventLog log) {
        eventLogMapper.save(log);
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
