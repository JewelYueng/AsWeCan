package org.k2.processmining.service.impl;

import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.mapper.RawLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogServiceImpl;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.k2.processmining.support.normal.transform.Normalize;
import org.k2.processmining.support.normal.transform.NormalizeException;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class RawLogServiceImpl extends CommonLogServiceImpl<RawLog> implements RawLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawLogServiceImpl.class);

    @Autowired
    private NormalLogService normalLogService;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private NormalLogMapper normalLogMapper;

    @Autowired
    public RawLogServiceImpl(RawLogMapper rawLogMapper, LogStorage logStorage) {
        super(rawLogMapper, logStorage);
    }

    @Override
    public List<LogGroup> getLogsByUser(User user, int page) {
        List<LogGroup> logGroups = super.getLogsByUser(user, page);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getLogsByUserAndKeyWord(User user, String keyWord, int page) {
        List<LogGroup> logGroups = super.getLogsByUserAndKeyWord(user, keyWord, page);
        verifyLogGroupsIsActive(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogs(int page) {
        List<LogGroup> logGroups = super.getSharedLogs(page);
        verifyLogGroupsIsShared(logGroups);
        return logGroups;
    }

    @Override
    public List<LogGroup> getSharedLogsByKeyWord(String keyWord, int page) {
        List<LogGroup> logGroups = super.getSharedLogsByKeyWord(keyWord, page);
        verifyLogGroupsIsShared(logGroups);
        return logGroups;
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
        normalLogMapper.deleteNormalLogByRawLogId(rawLog.getId());
        try {
            logStorage.upload(normalLog,
                    outputStream -> logStorage.download(rawLog,
                            inputStream -> {
                                try {
                                    Normalize.normalize(lc, inputStream, outputStream);
                                }
                                catch (NormalizeException e) {
                                    LOGGER.error("Fail to normalize rawLog<{}>", rawLog.getId(), e);
                                    throw new BadRequestException(Message.NORMALIZE_FAIL);
                                }
                                return true;
                            }
                    )
            );
        }
        catch (IOException e) {
            LOGGER.error("Fail to normalize rawLog<{}>", rawLog.getId(), e);
            throw new InternalServerErrorException(Message.NORMALIZE_FAIL);
        }
        normalLogService.save(normalLog);
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
