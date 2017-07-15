package org.k2.processmining.service.impl;

import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.mapper.NormalLogMapper;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogServiceImpl;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.transform.TransToEvent;
import org.k2.processmining.support.event.transform.TransToEventException;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class NormalLogServiceImpl extends CommonLogServiceImpl<NormalLog> implements NormalLogService {

    private static Logger LOGGER = LoggerFactory.getLogger(NormalLogServiceImpl.class);

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    public NormalLogServiceImpl(NormalLogMapper normalLogMapper, LogStorage logStorage) {
        super(normalLogMapper, logStorage);
    }

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
        File file;
        try {
            file = logStorage.download(normalLog, inputStream -> {
                try {
                    return TransToEvent.transToEvent(inputStream, tmpdir, name);
                }
                catch (TransToEventException e) {
                    LOGGER.error("Fail to transform to eventLog for normalLog<{}>:", normalLog.getId(), e);
                    throw new BadRequestException(Message.TRANS_TO_EVENT_LOG_FAIL);
                }
            });
        }
        catch (IOException e) {
            LOGGER.error("Fail to transform to eventLog for normalLog<{}>:", normalLog.getId(), e);
            throw new InternalServerErrorException(Message.TRANS_TO_EVENT_LOG_FAIL);
        }

        eventLogMapper.deleteEventLogByNormalLogId(normalLog.getId());

        try (InputStream inputForRemote = new FileInputStream(file);
             InputStream inputForSummarize = new FileInputStream(file)){
            eventLogService.save(eventLog, inputForRemote, inputForSummarize);
        }
        catch (IOException e) {
            LOGGER.error("Fail to save eventLog:", e);
            throw new InternalServerErrorException();
        }
        finally {
            file.deleteOnExit();
        }
        return eventLog;
    }
}
