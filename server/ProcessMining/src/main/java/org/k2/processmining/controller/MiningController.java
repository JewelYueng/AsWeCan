package org.k2.processmining.controller;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody Object getAllMiningMethods(){
        Map<String, Object> res = new HashMap<>();
        List<Object> methodsConfigs = new LinkedList<>();
        List<MiningMethod> methods = miningMethodService.getActiveMethods();
        for (MiningMethod method : methods) {
            Map<String, Object> configs = miningMethodService.getMethodConfig(method);
            if (configs != null && configs.size() > 0) {
                configs.put("state", method.getState());
                methodsConfigs.add(configs);
            }
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public@ResponseBody Object mining(@Valid @RequestBody MiningForm form) {

        MiningMethod miningMethod = miningMethodService.getMethodById(form.methodId);
        if (miningMethod != null && miningMethodService.isActive(miningMethod)) {
            // TODO: 2017/6/17 check whether the eventLog belongs to user
            EventLog eventLog = new EventLog();
            eventLog.setId(form.id);
            eventLog.setUserId("1");
            return miningMethodService.mining(eventLog, form.methodId, form.parameters);
        }
        return null;
    }

    public void addMiningMethod(){}

    public void setMethodState(){}  //禁用或者重新激活算法

    public void deleteMiningMethod(){}

    public static class MiningForm {
        private String id;
        private String methodId;
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

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
}

