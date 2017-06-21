package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.mapper.RawLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.k2.processmining.support.normal.transform.Normalize;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class RawLogServiceImpl implements RawLogService {

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private RawLogMapper rawLogMapper;

    @Autowired
    private NormalLogMapper normalLogMapper;

    @Override
    public RawLog getRawLogById(String id) {
        return rawLogMapper.getRawLogById(id);
    }

    @Override
    public List<LogGroup> getLogsByUser(User user) {
        List<LogGroup> logGroups = rawLogMapper.listLogGroups(user.getId(), LogState.ACTIVE.getValue(),-1,null);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    public List<LogGroup> getSharedLogs() {
        List<LogGroup> logGroups = rawLogMapper.listLogGroups(null,LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue(), null);
        verifyLogGroupsIsShared(logGroups);
        return logGroups;
    }

    /**
     * 通过关键字搜索日志
     * @param keyWord
     * @param user
     * @return
     */
    @Override
    public List<LogGroup> getLogByFuzzyName(String keyWord,User user) {
        List<LogGroup> logGroups = rawLogMapper.listLogGroups(user.getId(), LogState.ACTIVE.getValue(), -1, keyWord);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    @Override
    public boolean save(RawLog log, InputStream inputStream) {
        if (logStorage.upload(log, inputStream)) {
            afterSaveInLogStorage(log);
            return true;
        }
        return false;
    }

    @Override
    public void afterSaveInLogStorage(RawLog log) {
        rawLogMapper.save(log);
    }

    @Override
    public void afterSaveInLogStorageForNormalize(NormalLog normalLog, RawLog rawLog) {
        normalLogMapper.deleteNormalLogByRawLogId(rawLog.getId());
        normalLogMapper.save(normalLog);
    }

    @Override
    public void updateShareStateByLogIdForUser(List<String> ids, int isShared, String userId) {
        rawLogMapper.updateIsShared(ids, isShared, userId);
    }

    @Override
    public void updateStateByLogIdForUser(List<String> ids, int state, String userId) {
        rawLogMapper.updateLogState(ids, state, userId);
    }

    @Override
    public NormalLog normalize(RawLog rawLog, LogConfiguration lc) {
        NormalLog normalLog = new NormalLog();
        normalLog.setId(Util.getUUIDString());
        normalLog.setLogName(Util.getNormalizeName(rawLog.getLogName()));
        normalLog.setUserId(rawLog.getUserId());
        normalLog.setRawLogId(rawLog.getId());
        normalLog.setCreateDate(new Date());
        normalLog.setFormat("txt");
        if(! logStorage.upload(normalLog,
                outputStream -> logStorage.download(rawLog,
                        inputStream -> Normalize.normalize(lc, inputStream, outputStream)))) {
            return null;
        }
        afterSaveInLogStorageForNormalize(normalLog, rawLog);
        return normalLog;
    }

    private void verifyLogGroupsIsActive(List<LogGroup> logGroups){
        Iterator<LogGroup> iterator =logGroups.iterator();
        LogGroup logGroup;
        Set<String> rawLogIdSet = new HashSet<>();
        while (iterator.hasNext()){
            logGroup = iterator.next();
            if (logGroup.getRawLog() == null || rawLogIdSet.contains(logGroup.getRawLog().getId())){
                iterator.remove();
                continue;
            }
            rawLogIdSet.add(logGroup.getRawLog().getId());
            if (!Util.isActive(logGroup.getNormalLog())){
                logGroup.setNormalLog(null);
            }
            if (!Util.isActive(logGroup.getEventLog())){
                logGroup.setEventLog(null);
            }
        }
    }

    private void verifyLogGroupsIsShared(List<LogGroup> logGroups) {
        Iterator<LogGroup> iterator = logGroups.iterator();
        Set<String> rawLogIdSet = new HashSet<>();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (logGroup.getRawLog() == null || rawLogIdSet.contains(logGroup.getRawLog().getId())) {
                iterator.remove();
                continue;
            }
            rawLogIdSet.add(logGroup.getRawLog().getId());
            if (! Util.isActiveAndShared(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
            }
            if (! Util.isActiveAndShared(logGroup.getEventLog())) {
                logGroup.setEventLog(null);
            }

        }
    }
}
