package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.NormalLogMapper;
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
import java.util.*;

/**
 * Created by nyq on 2017/6/17.
 */

@Service
public class NormalLogServiceImpl implements NormalLogService {

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private NormalLogMapper normalLogMapper;

    @Override
    public EventLog transToEventLog(NormalLog normalLog) {
        EventLog eventLog = new EventLog();
        // set eventLogId and userId
        eventLog.setUserId(normalLog.getUserId());
        eventLog.setId(UUID.randomUUID().toString());
        String tmpdir = System.getProperty("java.io.tmpdir");
        String name = eventLog.getId();
        File file = logStorage.download(normalLog, inputStream -> TransToEvent.transToEvent(inputStream, tmpdir, name));
        if (file == null || !file.isFile()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            if (!logStorage.upload(eventLog, inputStream)) {
                // TODO: 2017/6/17 save eventLog to database

                return eventLog;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户的规范化日志列表
     * @param userId
     * @return
     */
    @Override
    public List<NormalLog> getNormalLogsByUserId(String userId) {

        if (userId == null)
            return null;
        return normalLogMapper.getNormalLogsByUserId(userId);
    }


    /**
     * 获取分享的规范化日志列表
     * @return
     */
    @Override
    public List<NormalLog> getAllSharedNormalLogs() {
        return normalLogMapper.getAllSharedNormalLogs();
    }

    @Override
    public int updateShareStateByLogId(List<String> idList,int isshared) {

        for (String id : idList) {
            NormalLog normalLog = new NormalLog();
            normalLog.setId(id);
            normalLog.setState(isshared);//分享
            normalLogMapper.updateShareStateByLogId(normalLog);
        }
        return 1;
    }

    @Override
    public int deleteLogByLogId(List<String> idList) {
        return 0;
    }

    @Override
    public void addNormalLog(NormalLog normalLog) {
        System.out.println("id:"+normalLog.getId());
        normalLogMapper.save(normalLog);
    }
}
