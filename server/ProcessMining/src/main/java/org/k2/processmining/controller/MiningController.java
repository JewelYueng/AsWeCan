package org.k2.processmining.controller;

import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
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
 * Created by Aria on 2017/6/9.
 */
@Controller
@RequestMapping("/mining")
public class MiningController {

    @Autowired
    MiningMethodService miningMethodService;

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody Object getAllMiningMethods(){
        Map<String, Object> res = new HashMap<>();
        List<Object> methodsConfigs = new LinkedList<>();
        List<MiningMethod> methods = miningMethodService.getActiveMethods();
        for (MiningMethod method : methods) {
            Map<String, Object> configs = miningMethodService.getMethodConfig(method);
            configs.put("state", method.getState());
            methodsConfigs.add(configs);
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public@ResponseBody Object mining(@RequestParam("id") String id, @RequestParam("methodId") String methodId) {

        if (!miningMethodService.isActive(methodId)) {
            System.out.println("method is freeze");
        }
        // check whether the eventLog belongs to user

        return miningMethodService.mining("1", id, methodId);
    }

    public void addMiningMethod(){}

    public void setMethodState(){}  //禁用或者重新激活算法

    public void deleteMiningMethod(){}
}

