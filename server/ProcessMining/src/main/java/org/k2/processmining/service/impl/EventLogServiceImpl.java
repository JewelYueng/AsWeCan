package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogServiceImpl;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.parse.EventLogParseException;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by nyq on 2017/6/19.
 */
@Service
public class EventLogServiceImpl extends CommonLogServiceImpl<EventLog> implements EventLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogServiceImpl.class);

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogParse eventLogParse;

    @Autowired
    public EventLogServiceImpl(EventLogMapper eventLogMapper, LogStorage logStorage) {
        super(eventLogMapper, logStorage);
    }

    @Override
    public List<LogGroup> getLogsByUser(User user, int page) {
        List<LogGroup> logGroups = super.getLogsByUser(user, page);
        verifyLogGroupsIsActive(logGroups);
        addMergeRelationEventLogs(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getLogsByUserAndKeyWord(User user, String keyWord, int page) {
        List<LogGroup> logGroups = super.getLogsByUserAndKeyWord(user, keyWord, page);
        verifyLogGroupsIsActive(logGroups);
        addMergeRelationEventLogs(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogs(int page) {
        List<LogGroup> logGroups = super.getSharedLogs(page);
        verifyLogGroupsIsShared(logGroups);
        addMergeRelationEventLogs(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogsByKeyWord(String keyWord, int page) {
        List<LogGroup> logGroups = super.getSharedLogsByKeyWord(keyWord, page);
        verifyLogGroupsIsShared(logGroups);
        addMergeRelationEventLogs(logGroups);
        return logGroups;
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
            throw new BadRequestException(Message.INVALID_EVENT_LOG);
        }
        eventLogMapper.save(log);
    }

    @Override
    public void save(EventLog log, InputStream inputStream) throws IOException {
        throw new UnsupportedOperationException("may implement later, but not now");
    }

    @Override
    public List<EventLog> getEventLogsByIds(List<String> ids) {
        return eventLogMapper.listEventLogByIds(ids);
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

    private void addMergeRelationEventLogs(List<LogGroup> logGroups) {
        Set<String> mergeEventLogIds = new HashSet<>();
        for (LogGroup logGroup : logGroups) {
            String mergeRelations = logGroup.getEventLog().getMergeRelation();
            if (mergeRelations != null) {
                mergeEventLogIds.addAll(Arrays.asList(mergeRelations.split(",")));
            }
        }
        if (mergeEventLogIds.isEmpty()) return;
        List<EventLog> eventLogs = eventLogMapper.listEventLogByIds(new ArrayList<>(mergeEventLogIds));
        Map<String,EventLog> map = new HashMap<>();
        eventLogs.forEach(eventLog -> map.put(eventLog.getId(), eventLog));
        for (LogGroup logGroup : logGroups) {
            String mergeRelations = logGroup.getEventLog().getMergeRelation();
            if (mergeRelations != null) {
                ArrayList<EventLog> mergeEventLogs = new ArrayList<>(2);
                for (String id :mergeRelations.split(",")) {
                    mergeEventLogs.add(map.get(id));
                }
                logGroup.getEventLog().setMergeRelationLogs(mergeEventLogs);
            }
        }

    }
}
