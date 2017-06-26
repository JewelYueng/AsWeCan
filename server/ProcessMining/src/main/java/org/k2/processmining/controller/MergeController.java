package org.k2.processmining.controller;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.service.TimeResult;
import org.k2.processmining.service.impl.MergeMethodServiceImpl;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
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
        Map<String, Object> res = new HashMap<>();
        if (mergeMethod == null || ! mergeMethodService.isActive(mergeMethod)) {
            res.put("msg", "The merge algorithm is not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        User user = getUser();
        EventLog eventLog1 = eventLogService.getEventLogById(form.getEventLogId1());
        EventLog eventLog2 = eventLogService.getEventLogById(form.getEventLogId2());
        if (!Util.isActiveAndBelongTo(eventLog1, user) || !Util.isActiveAndBelongTo(eventLog2, user)) {
            res.put("msg", "The event logs are not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        TimeResult<EventLog> result = mergeMethodService.merge(eventLog1, eventLog2, form.methodId, form.parameters);
        if (result == null) {
            res.put("msg", "Fail to merge the logs. Please check the input content and try again!");
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(res);
        }
        res.put("timeCost", result.getTime());
        res.put("eventLog", result.getResult());
        return res;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    Object addMergeMethod(@Param("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("at least one file!");
        }
        Map<String,Object> res = new HashMap<>();
        try {
            MergeMethod mergeMethod = mergeMethodService.addMethod(files);
            res.put("state", mergeMethod.getState());
            res.put("id", mergeMethod.getId());
            res.put("configs", mergeMethodService.getMethodConfig(mergeMethod));
            return res;
        }
        catch (IOException | LoadMethodException e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public @ResponseBody
    Object active(@RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.ACTIVE.getValue());
    }

    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    public @ResponseBody
    Object freeze(@RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.FREEZE.getValue());
    }

    private Map<String,Object> setMethodState(List<String> ids, int state) {
        mergeMethodService.setMethodState(ids, state);
        Map<String,Object> res = new HashMap<>();
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@RequestBody IdListForm form) {
        mergeMethodService.delete(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

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
