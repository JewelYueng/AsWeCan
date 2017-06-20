package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.mapper.RawLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.k2.processmining.support.normal.transform.Normalize;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
        List<LogGroup> logGroups = rawLogMapper.listLogsByUserIdAndState(user.getId(), LogState.ACTIVE.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (!Util.isActive(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
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

    public List<LogGroup> getSharedLogs() {
        List<LogGroup> logGroups = rawLogMapper.listLogsByStateAndSharedState(LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue());
        Iterator<LogGroup> iterator = logGroups.iterator();
        while (iterator.hasNext()) {
            LogGroup logGroup = iterator.next();
            if (! Util.isActiveAndShared(logGroup.getNormalLog())) {
                logGroup.setNormalLog(null);
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
    public boolean save(RawLog log, InputStream inputStream) {
        if (logStorage.upload(log, inputStream)) {
            rawLogMapper.save(log);
            return true;
        }
        return false;
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
            RawLog rawLog = new RawLog();
            rawLog.setId(id);
            rawLog.setIsShared(isshared);//分享
            rawLogMapper.updateShareStateByLogId(rawLog);
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
            RawLog rawLog = new RawLog();
            rawLog.setId(id);
            rawLog.setState(state);
            rawLogMapper.updateLogStateByLogId(rawLog);
        }
        return 1;
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
        normalLogMapper.save(normalLog);
        return normalLog;
    }
}
