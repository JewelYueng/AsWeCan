package org.k2.processmining.service;

import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.support.normal.transform.LogConfiguration;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService {
    void save(RawLog log);
    void deleteLogById(List<Integer> idList);
    NormalLog normalize(RawLog rawLog, LogConfiguration lc);
}
