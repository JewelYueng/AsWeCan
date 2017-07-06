package org.k2.processmining.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.controller.annotion.Diagram;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.support.algorithm.Algorithm;
import org.k2.processmining.support.mining.Miner;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/9.
 */
@Controller
@RequestMapping("/mining")
public class MiningController {

    @Autowired
    private MiningMethodService miningMethodService;

    @Autowired
    private EventLogService eventLogService;

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody Object getAllMiningMethods(){
        Map<String, Object> res = new HashMap<>();
        List<Object> methodsConfigs = new LinkedList<>();
        List<MiningMethod> methods = miningMethodService.getActiveMethods();
        for (MiningMethod method : methods) {
            Map<String, Object> configs = miningMethodService.getMethodConfig(method);
            if (configs != null && configs.size() > 0) {
                configs.put("state", method.getState());
                configs.put("id", method.getId());
                methodsConfigs.add(configs);
            }
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public@ResponseBody Object mining(@Valid @RequestBody MiningForm form) {
        Map<String, Object> res = new HashMap<>();
        EventLog eventLog = eventLogService.getLogById(form.getId());
        Algorithm<Miner> algorithm = miningMethodService.getAlgorithmById(form.getMethodId());
        TimeResult timeResult =  miningMethodService.mining(eventLog, algorithm, form.parameters, Util.toDiagramType(form.diagramType));
        res.put("timeCost", timeResult.getTime());
        res.put("diagram", timeResult.getResult());
        return res;
    }

    public static class MiningForm {
        @NotBlank(message = "EventLogId is invalid.")
        private String id;
        @NotBlank(message = "MethodId is invalid.")
        private String methodId;
        @Diagram
        private String diagramType;
        @NotNull(message = "Params is invalid.")
        private Map<String, Object> parameters;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMethodId() {
            return methodId;
        }

        public void setMethodId(String methodId) {
            this.methodId = methodId;
        }

        public String getDiagramType() {
            return diagramType;
        }

        public void setDiagramType(String diagramType) {
            this.diagramType = diagramType;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
}

