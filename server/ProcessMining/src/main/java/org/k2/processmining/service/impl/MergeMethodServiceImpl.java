package org.k2.processmining.service.impl;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.mapper.EventLogMapper;
import org.k2.processmining.mapper.MergeMethodMapper;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.service.TimeResult;
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
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by nyq on 2017/6/17.
 */
@Service
public class MergeMethodServiceImpl implements MergeMethodService{

    private static Logger LOGGER = LoggerFactory.getLogger(MergeMethodServiceImpl.class);

    @Autowired
    private EventLogService eventLogService;

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
    public TimeResult<EventLog> merge(EventLog eventLog1, EventLog eventLog2, MergeMethod mergeMethod, Map<String, Object> params) {
        String methodId = mergeMethod.getId();
        Algorithm<Merger> algorithm = MergerFactory.getInstance().getAlgorithm(methodId);
        if (algorithm == null || algorithm.getAlgorithm() == null) {
            throw new BadRequestException(Message.METHOD_IS_NOT_EXIST);
        }
        XLog xLog1 = eventLogParse.eventLogParse(eventLog1);
        if (xLog1 == null) {
            throw new BadRequestException(Message.INVALID_EVENT_LOG);
        }
        XLog xLog2 = eventLogParse.eventLogParse(eventLog2);
        if (xLog2 == null) {
            throw new BadRequestException(Message.INVALID_EVENT_LOG);
        }
        TimeResult<EventLog> timeResult = new TimeResult<>();
        timeResult.start();
        XLog resultXLog;
        try {
            resultXLog = algorithm.getAlgorithm().merge(xLog1, xLog2, params);
        }
        catch (Exception e) {
            LOGGER.error("Fail to merge eventLog<{}> and eventLog<{}>:", eventLog1.getId(), eventLog2.getId(), e);
            throw new BadRequestException(Message.MERGE_FAIL);
        }
        timeResult.stop();
        if (resultXLog == null) {
            throw new BadRequestException(Message.MERGE_FAIL);
        }
        EventLog resultEventLog = new EventLog();
        resultEventLog.setId(Util.getUUIDString());
        resultEventLog.setUserId(eventLog1.getUserId());
        resultEventLog.setLogName(Util.getMergeName(eventLog1.getLogName(), eventLog2.getLogName(), mergeMethod.getMethodName()));
        resultEventLog.setCreateDate(new Date());
        resultEventLog.setFormat("xes");
        resultEventLog.setMergeRelation(eventLog1.getId() + "," + eventLog2.getId());
        resultEventLog.setMergeRelationLogs(Arrays.asList(eventLog1, eventLog2));
        try {
            logStorage.upload(resultEventLog, outputStream -> {
                eventLogExport.convertXLog(resultXLog, outputStream);
                return true;
            });
        }
        catch (IOException e) {
            LOGGER.error("Fail to convert resultEventLog:", e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        Summarize.summarizeTo(resultXLog, resultEventLog);
        eventLogService.save(resultEventLog);
        timeResult.setResult(resultEventLog);
        return timeResult;
    }

    @Override
    public MergeMethod saveMethod(MergeMethod mergeMethod, MultipartFile[] multipartFiles) throws IOException, LoadMethodException {

        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()){
                methodManage.saveMergerJar(mergeMethod.getId(), file.getOriginalFilename(), inputStream);
            }
        }
        Algorithm<Merger> mergerAlgorithm = methodManage.loadMergerById(mergeMethod.getId());
        mergeMethod.setMethodName((String)mergerAlgorithm.getConfigMap().get("key"));
        MergerFactory.getInstance().put(mergeMethod.getId(), mergerAlgorithm);
        mergeMethodMapper.save(mergeMethod);
        return mergeMethod;
    }

    @Override
    public void updateMethodState(List<String> ids, int state) {
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
}
