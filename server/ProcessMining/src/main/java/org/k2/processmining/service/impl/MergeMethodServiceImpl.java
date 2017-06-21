package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.mapper.MergeMethodMapper;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.event.export.EventLogExport;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.support.merge.Merger;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class MergeMethodServiceImpl implements MergeMethodService{

    @Autowired
    private MergeMethodService mergeMethodService;

    @Autowired
    private MergeMethodMapper mergeMethodMapper;

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    private EventLogParse eventLogParse;

    @Autowired
    private LogStorage logStorage;

    @Autowired
    private EventLogExport eventLogExport;

    @Override
    public MergeMethod getMethodById(String id) {
        return mergeMethodMapper.getMethodById(id);
    }

    @Override
    public List<MergeMethod> getActiveMethods() {
        return mergeMethodMapper.listMethodByState(LogState.ACTIVE.getValue());
    }

    @Override
    public List<MergeMethod> getAllMethods() {
        return mergeMethodMapper.listMethods();
    }

    @Override
    public Map<String, Object> getMethodConfig(MergeMethod mergeMethod) {
        return mergeMethod == null ? Collections.emptyMap() : getMethodConfig(mergeMethod.getId());
    }

    @Override
    public Map<String, Object> getMethodConfig(String methodId) {
        Algorithm<Merger> algorithm = MergerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null) {
            return Collections.emptyMap();
        }
        return algorithm.getConfigMap();
    }

    @Override
    public boolean isActive(String id) {
        MergeMethod mergeMethod = getMethodById(id);
        return mergeMethod != null && MethodState.isActive(mergeMethod.getState());
    }

    @Override
    public boolean isActive(MergeMethod mergeMethod) {
        return mergeMethod != null && isActive(mergeMethod.getId());
    }


    @Override
    public EventLog merge(EventLog eventLog1, EventLog eventLog2, String methodId, Map<String, Object> params) {
        Algorithm<Merger> algorithm = MergerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null || algorithm.getAlgorithm() == null) {
            return null;
        }
        XLog xLog1 = logStorage.download(eventLog1, inputStream -> eventLogParse.eventLogParse(inputStream));
        if (xLog1 == null) {
            return null;
        }
        XLog xLog2 = logStorage.download(eventLog2, inputStream -> eventLogParse.eventLogParse(inputStream));
        if (xLog2 == null) {
            return null;
        }
        XLog resultXLog =  algorithm.getAlgorithm().merge(xLog1, xLog2, params);
        if (resultXLog == null) {
            return null;
        }
        EventLog resultEventLog = new EventLog();
        resultEventLog.setId(Util.getUUIDString());
        resultEventLog.setUserId(eventLog1.getUserId());
        resultEventLog.setLogName(Util.getMergeName(eventLog1.getLogName(), eventLog2.getLogName()));
        resultEventLog.setCreateDate(new Date());
        resultEventLog.setFormat("xes");
        resultEventLog.setMergeRelation(eventLog1.getId() + "," + eventLog2.getId());
        if (! logStorage.upload(resultEventLog, outputStream -> eventLogExport.convertXLog(resultXLog, outputStream))) {
            return null;
        }
        mergeMethodService.afterSaveInLogStorage(resultEventLog, resultXLog);
        return resultEventLog;
    }

    @Override
    public void afterSaveInLogStorage(EventLog resultEventLog, XLog resultXLog) {
        EventLogSummary eventLogSummary = Summarize.summarizeXLog(resultXLog);
        resultEventLog.setCaseNumber(eventLogSummary.getCases());
        resultEventLog.setEventNames(eventLogSummary.getEventNames());
        resultEventLog.setEventNumber(eventLogSummary.getEvents());
        resultEventLog.setOperatorNames(eventLogSummary.getOperatorNames());
        eventLogMapper.save(resultEventLog);
    }
}
