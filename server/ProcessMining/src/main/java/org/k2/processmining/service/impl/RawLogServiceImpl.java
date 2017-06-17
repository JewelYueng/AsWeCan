package org.k2.processmining.service.impl;

import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.k2.processmining.support.normal.transform.Normalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class RawLogServiceImpl implements RawLogService {

    @Autowired
    private LogStorage logStorage;

    @Override
    public void save(RawLog log) {

    }

    @Override
    public void deleteLogById(List<Integer> idList) {

    }

    @Override
    public NormalLog normalize(RawLog rawLog, LogConfiguration lc) {
        NormalLog normalLog = new NormalLog();
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
