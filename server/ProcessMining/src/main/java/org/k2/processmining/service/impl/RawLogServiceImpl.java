package org.k2.processmining.service.impl;

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

    @Override
    public RawLog getRawLogById(String id) {
        return rawLogMapper.getRawLogById(id);
    }

    @Override
    public List<LogGroup> getLogsByUser(User user) {
        List<LogGroup> logGroups = rawLogMapper.listLogsByUserIdAndState(user.getId(), LogState.ACTIVE.getValue());
        for (LogGroup logGroup : logGroups) {
            if (logGroup.getNormalLog() != null && LogState.isActive(logGroup.getNormalLog().getState())) {
                logGroup.setNormalLog(null);
            }
            if (logGroup.getEventLog() != null && LogState.isActive(logGroup.getEventLog().getState())) {
                logGroup.setEventLog(null);
            }
        }
        return logGroups;
    }

    public List<LogGroup> getSharedLogs() {
        return rawLogMapper.listLogsByStateAndSharedState(LogState.ACTIVE.getValue(), LogShareState.SHARED.getValue());
    }

    @Override
    public boolean save(RawLog log, InputStream inputStream) {
        if (logStorage.upload(log, inputStream)) {
            rawLogMapper.save(log);
            return true;
        }
        return false;
    }

    @Override
    public void deleteLogById(List<Integer> idList) {

    }

    @Override
    public NormalLog normalize(RawLog rawLog, LogConfiguration lc) {
        NormalLog normalLog = new NormalLog();
        normalLog.setId(Util.getUUIDString());
        normalLog.setUserId(rawLog.getUserId());
        if(! logStorage.upload(normalLog,
                outputStream -> logStorage.download(rawLog,
                        inputStream -> Normalize.normalize(lc, inputStream, outputStream)))) {
            return null;
        }
        normalLog.setRawLogId(rawLog.getId());
        // TODO: 2017/6/17 save normalLog to database
        return normalLog;
    }
}
