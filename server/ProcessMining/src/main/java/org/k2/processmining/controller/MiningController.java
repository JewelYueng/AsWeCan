package org.k2.processmining.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Aria on 2017/6/9.
 */
@RequestMapping("/mining")
public class MiningController {

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody Object getAllMiningMethods(){
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void mining(){}

    public void addMiningMethod(){}

    public void setMethodState(){}  //禁用或者重新激活算法

    public void deleteMiningMethod(){}
}

