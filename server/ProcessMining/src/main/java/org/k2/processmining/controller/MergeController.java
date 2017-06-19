package org.k2.processmining.controller;

import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Autowired
    private EventLogService eventLogService;

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
    public @ResponseBody Object merge(@Valid @RequestBody MergeMethodForm form) {
        MergeMethod mergeMethod = mergeMethodService.getMethodById(form.methodId);
        Map<String, Object> res = new HashMap<>();
        if (mergeMethod == null || ! mergeMethodService.isActive(mergeMethod)) {
            res.put("msg", "The merge algorithm is not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        User user = getUser();
        EventLog eventLog1 = eventLogService.getEventLogById(form.getEventLogId1());
        EventLog eventLog2 = eventLogService.getEventLogById(form.getEventLogId2());
        if (!isValidate(eventLog1, user) || !isValidate(eventLog2, user)) {
            res.put("msg", "The event logs are not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        long start = System.currentTimeMillis(); // have to modify
        EventLog result = mergeMethodService.merge(eventLog1, eventLog2, form.methodId, form.parameters);

        if (result == null) {
            res.put("msg", "Fail to merge the logs. Please check the input content and try again!");
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(res);
        }
        res.put("timeCost", System.currentTimeMillis()-start);
        res.put("eventLog", result);
        return res;
    }

    public void addMergeMethod(){}

    public void setMethodState(){}

    public void deleteMergeMethod(){}

    private boolean isValidate(EventLog eventLog, User user) {
        return eventLog != null && user != null
                && eventLog.getUserId() != null && user.getId() != null
                && eventLog.getUserId().equals(user.getId())
                && eventLog.getState() == LogState.ACTIVE.getValue();
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }

    public static class MergeMethodForm {
        @NotNull
        @Size(min = 1, max = 36)
        private String methodId;
        @NotNull
        @Size(min = 1, max = 36)
        private String eventLogId1;
        @NotNull
        @Size(min = 1, max = 36)
        private String eventLogId2;
        private Map<String, Object> parameters;

        public String getMethodId() {
            return methodId;
        }

        public void setMethodId(String methodId) {
            this.methodId = methodId;
        }

        public String getEventLogId1() {
            return eventLogId1;
        }

        public void setEventLogId1(String eventLogId1) {
            this.eventLogId1 = eventLogId1;
        }

        public String getEventLogId2() {
            return eventLogId2;
        }

        public void setEventLogId2(String eventLogId2) {
            this.eventLogId2 = eventLogId2;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
}
