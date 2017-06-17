package org.k2.processmining.controller;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.MergeMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aria on 2017/6/13.
 */

@Controller
@RequestMapping("/merge")
public class MergeController {

    @Autowired
    private MergeMethodService mergeMethodService;

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody Object getAllMergeMethods() {
        Map<String, Object> res = new HashMap<>();
        List<Object> methodsConfigs = new LinkedList<>();
        List<MergeMethod> methods = mergeMethodService.getActiveMethods();
        for (MergeMethod method : methods) {
            Map<String, Object> configs = mergeMethodService.getMethodConfig(method);
            if (configs != null && configs.size() > 0) {
                configs.put("state", method.getState());
                methodsConfigs.add(configs);
            }
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody Object merge(@RequestParam("eventLogId1") String eventLogId1,
                                      @RequestParam("eventLogId2") String eventLogId2,
                                      @RequestParam("methodId") String methodId,
                                      @RequestParam("parameters") Map<String, Object> parameters) {
        MergeMethod mergeMethod = mergeMethodService.getMethodById(methodId);
        if (mergeMethod != null && mergeMethodService.isActive(mergeMethod)) {
            // TODO: 2017/6/17 check eventLog owner

            EventLog eventLog1 = new EventLog();
            EventLog eventLog2 = new EventLog();
            EventLog result = mergeMethodService.merge(eventLog1, eventLog2, methodId, parameters);
            if (result != null) {
                return result;
            }
        }

        // TODO: 2017/6/17 set response code and error message
        return "";
    }

    public void addMergeMethod(){}

    public void setMethodState(){}

    public void deleteMergeMethod(){}
}
