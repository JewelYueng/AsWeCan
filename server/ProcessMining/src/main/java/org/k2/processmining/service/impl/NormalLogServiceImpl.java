package org.k2.processmining.service.impl;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.event.transform.TransToEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by nyq on 2017/6/17.
 */

@Service
public class NormalLogServiceImpl implements NormalLogService {

    @Autowired
    private LogStorage logStorage;

    @Override
    public EventLog transToEventLog(NormalLog normalLog) {
        EventLog eventLog = new EventLog();
        // set eventLogId and userId
        eventLog.setUserId(normalLog.getUserId());
        eventLog.setId(UUID.randomUUID().toString());
        String tmpdir = System.getProperty("java.io.tmpdir");
        String name = eventLog.getId();
        File file = logStorage.download(normalLog, inputStream -> TransToEvent.transToEvent(inputStream, tmpdir, name));
        if (file == null || ! file.isFile()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)){
            if (! logStorage.upload(eventLog, inputStream)) {
                // TODO: 2017/6/17 save eventLog to database

                return eventLog;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
