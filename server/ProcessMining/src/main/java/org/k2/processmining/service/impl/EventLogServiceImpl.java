package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

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
        List<LogGroup> logGroups = eventLogMapper.listLogsByUserIdAndState(userId, LogState.ACTIVE.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (!Util.isActive(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (!Util.isActive(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
            }
            if (logGroup.getRawLog() == null && logGroup.getNormalLog() == null && logGroup.getEventLog() == null) {
                iterator.remove();
            }
        }
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogs() {
        List<LogGroup> logGroups = eventLogMapper.listLogsByStateAndSharedState(LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (! Util.isActiveAndShared(logGroup.getRawLog())) {
                logGroup.setRawLog(null);
            }
            if (! Util.isActiveAndShared(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
            }
            if (logGroup.getRawLog() == null && logGroup.getNormalLog() == null && logGroup.getEventLog() == null) {
                iterator.remove();
            }
        }
        return logGroups;
    }

    /**
     * 更新日志的分享状态
     * @param idList
     * @param isshared
     * @return
     */
    @Override
    public int updateShareStateByLogId(List<String> idList,int isshared) {

        for (String id : idList) {
            EventLog eventLog = new EventLog();
            eventLog.setId(id);
            eventLog.setIsShared(isshared);//分享
            eventLogMapper.updateShareStateByLogId(eventLog);
        }
        return 1;
    }

    /**
     * 更新日志的状态
     * @param idList
     * @param state
     * @return
     */
    @Override
    public int updateStateByLogId(List<String> idList, int state) {
        for (String id: idList){
            EventLog eventLog = new EventLog();
            eventLog.setId(id);
            eventLog.setState(state);
            eventLogMapper.updateLogStateByLogId(eventLog);
        }
        return 1;
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
        eventLogMapper.save(log);
        return true;
    }
}
