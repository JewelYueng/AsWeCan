package org.k2.processmining.controller;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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
        MiningMethod miningMethod = miningMethodService.getMethodById(form.getMethodId());
        if (miningMethod == null || !miningMethodService.isActive(miningMethod)) {
            res.put("code", 0);
            res.put("msg", "The merge algorithm is not exist!");
            return res;
        }
        EventLog eventLog = eventLogService.getEventLogById(form.getId());
        User user = getUser();
        if (!Util.isActiveAndBelongTo(eventLog, user)) {
            res.put("code", 0);
            res.put("msg", "The event log is not exist!");
            return res;
        }
        return miningMethodService.mining(eventLog, form.methodId, form.parameters);
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    Object addMiningMethod(@Param("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("at least one file!");
        }
        Map<String,Object> res = new HashMap<>();
        try {
            MiningMethod miningMethod = miningMethodService.addMethod(files);
            res.put("state", miningMethod.getState());
            res.put("id", miningMethod.getId());
            res.put("configs", miningMethodService.getMethodConfig(miningMethod));
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
        miningMethodService.setMethodState(ids, state);
        Map<String,Object> res = new HashMap<>();
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@RequestBody IdListForm form) {
        miningMethodService.delete(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

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

