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
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.support.algorithm.MergerFactory;
import org.k2.processmining.support.algorithm.MethodManage;
import org.k2.processmining.support.event.export.EventLogExport;
import org.k2.processmining.support.event.parse.EventLogParse;
import org.k2.processmining.support.event.sumarise.EventLogSummary;
import org.k2.processmining.support.event.sumarise.Summarize;
import org.k2.processmining.support.merge.Merger;
import org.k2.processmining.support.reflect.ReflectUtil;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    private MethodManage methodManage;

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
    public MergeResult merge(EventLog eventLog1, EventLog eventLog2, String methodId, Map<String, Object> params) {
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
        long cost = System.currentTimeMillis();
        XLog resultXLog =  algorithm.getAlgorithm().merge(xLog1, xLog2, params);
        cost = System.currentTimeMillis() - cost;
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
        return new MergeResult(resultEventLog, cost);
    }

    @Override
    public MergeMethod addMethod(MultipartFile[] multipartFiles) throws IOException, LoadMethodException {
        MergeMethod mergeMethod = new MergeMethod();
        mergeMethod.setId(Util.getUUIDString());

        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()){
                methodManage.saveMergerJar(mergeMethod.getId(), file.getOriginalFilename(), inputStream);
            }
        }
        Algorithm<Merger> mergerAlgorithm = methodManage.loadMergerById(mergeMethod.getId());
        mergeMethod.setMethodName((String)mergerAlgorithm.getConfigMap().get("key"));
        MergerFactory.getInstance().put(mergeMethod.getId(), mergerAlgorithm);
        mergeMethodService.afterSaveMethod(mergeMethod);
        return mergeMethod;
    }

    @Override
    public void setMethodState(List<String> ids, int state) {
        mergeMethodMapper.updateState(ids, state);
    }

    @Override
    public void delete(List<String> ids) {
        mergeMethodMapper.delete(ids);
        for (String id : ids) {
            MergerFactory.getInstance().deleteAlgorithm(id);
            ReflectUtil.getInstance().closeClassLoader(id);
        }
        System.gc();
//        for (String id :ids) {
//            methodManage.deleteMerger(id);
//        }
    }

    @Override
    public void afterSaveMethod(MergeMethod mergeMethod) {
        mergeMethodMapper.save(mergeMethod);
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

    // a little ugly
    public static class MergeResult {
        private EventLog eventLog;
        private long cost;
        public MergeResult() {}
        public MergeResult(EventLog eventLog, long cost) { this.eventLog = eventLog; this.cost = cost; }
        public EventLog getEventLog() {return eventLog;}
        public void setEventLog(EventLog eventLog) {this.eventLog = eventLog;}
        public long getCost() {return cost;}
        public void setCost(long cost) {this.cost = cost;}
    }
}
