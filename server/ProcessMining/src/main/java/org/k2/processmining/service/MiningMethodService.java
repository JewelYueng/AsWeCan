package org.k2.processmining.service;

import org.deckfour.xes.model.XLog;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.support.mining.algorithm.heuristics.models.SimpleHeuristicsNet;
import org.k2.processmining.support.mining.model.DiagramType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
public interface MiningMethodService {
    MiningMethod getMethodById(String id);
    Algorithm<Miner> getAlgorithmById(String id);
    List<MiningMethod> getActiveMethods();
    List<MiningMethod> getAllMethods();
    Map<String, Object> getMethodConfig(MiningMethod miningMethod);
    Map<String, Object> getMethodConfig(String methodId);
    boolean isActive(String id);
    boolean isActive(MiningMethod miningMethod);

    TimeResult mining(EventLog eventLog, Algorithm<Miner> algorithm, Map<String,Object> params, DiagramType type);
    TimeResult<SimpleHeuristicsNet> mining(Algorithm<Miner> algorithm, EventLog eventLog, XLog xLog, Map<String,Object> params);

    MiningMethod addMethod(MiningMethod miningMethod, MultipartFile[] multipartFiles) throws IOException, LoadMethodException;
    void setMethodState(List<String> ids, int state);

    @Transactional
    void delete(List<String> ids);
}
