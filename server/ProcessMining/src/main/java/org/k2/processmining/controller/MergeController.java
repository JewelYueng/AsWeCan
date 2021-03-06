package org.k2.processmining.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

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
                configs.put("id", method.getId());
                methodsConfigs.add(configs);
            }
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody Object merge(@Valid @RequestBody MergeMethodForm form) {
        MergeMethod mergeMethod = mergeMethodService.getMethodById(form.methodId);
        EventLog eventLog1 = eventLogService.getLogById(form.getEventLogId1());
        EventLog eventLog2 = eventLogService.getLogById(form.getEventLogId2());
        TimeResult<EventLog> result = mergeMethodService.merge(eventLog1, eventLog2, mergeMethod, form.parameters);
        if (result == null) {
            throw new BadRequestException(Message.MERGE_FAIL);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("timeCost", result.getTime());
        res.put("eventLog", result.getResult());
        return res;
    }

    public static class MergeMethodForm {
        @NotBlank(message = "The methodId is invalid.")
        private String methodId;
        @NotBlank(message = "The eventLogId1 is invalid.")
        private String eventLogId1;
        @NotBlank(message = "The eventLogId1 is invalid.")
        private String eventLogId2;
        @NotNull(message = "Params is invalid.")
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
