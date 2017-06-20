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
     * 根据关键字搜索日志
     * @param keyWord
     * @param user
     * @return
     */
    @Override
    public List<LogGroup> getLogByFuzzyName(String keyWord, User user) {
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("keyWord",keyWord);
        request.put("userid",user.getId());
        List<LogGroup> logGroups = eventLogMapper.listLogsByFuzzyName(request);
        System.out.println("logGroups:"+logGroups.size());
        Iterator<LogGroup> iterator = logGroups.iterator();
        verificateLogGroups(iterator);
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
        eventLogMapper.updateShareStateByLogId(isshared,idList);
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
        eventLogMapper.updateLogStateByLogId(state,idList);
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

    private void verificateLogGroups(Iterator iterator){
        while (iterator.hasNext()){
            LogGroup logGroup = (LogGroup) iterator.next();
            if (!Util.isActive(logGroup.getRawLog())){
                logGroup.setRawLog(null);
            }
            if (!Util.isActive(logGroup.getNormalLog())){
                logGroup.setEventLog(null);
            }
            if (logGroup.getRawLog() == null && logGroup.getNormalLog() == null && logGroup.getEventLog() == null){
                iterator.remove();
            }
        }
    }
}
