package org.k2.processmining.service;

import org.k2.processmining.model.log.RawLog;

import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService {
    void save(RawLog log);
    void deleteLogById(List<Integer> idList);
}
