package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.mapper.MergeMethodMapper;
import org.k2.processmining.model.State;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.event.export.EventLogExport;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.support.merge.Merger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class MergeMethodServiceImpl implements MergeMethodService{

    @Autowired
    private MergeMethodMapper mergeMethodMapper;

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
        return mergeMethodMapper.listMethodByState(State.ACTIVE.getValue());
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
        return isActive(getMethodById(id));
    }

    @Override
    public boolean isActive(MergeMethod mergeMethod) {
        return mergeMethod != null && mergeMethod.getState() == State.ACTIVE.getValue();
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
        if (logStorage.upload(resultEventLog, outputStream -> eventLogExport.convertXLog(resultXLog, outputStream))) {
            EventLogSummary eventLogSummary = Summarize.summarizeXLog(resultXLog);
            resultEventLog.setCaseNumber(eventLogSummary.getCases());
            resultEventLog.setEventNames(eventLogSummary.getEventNames());
            resultEventLog.setEventNumber(eventLogSummary.getEvents());
            resultEventLog.setOperatorNames(eventLogSummary.getOperatorNames());

            resultEventLog.setMergeRelation(eventLog1.getId() + "," + eventLog2.getId());
            // TODO: 2017/6/17 save resultEventLog in database, if fail delete eventLog from file system

            return resultEventLog;
        }
        return null;
    }
}
