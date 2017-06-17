package org.k2.processmining.controller;

import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/rawLog")
public class RawLogController {

    @Autowired
    private RawLogService rawLogService;

    public void getAllLogs(){}

    public void upload(){}

    public void download(){}

    public void deleteLogById(List<Integer> idList){}

    @RequestMapping(value = "/normalize", method = RequestMethod.POST)
    public @ResponseBody
    NormalLog normalize(@Valid @RequestBody NormalizeRawLogConfigForm form) {
        RawLog rawLog = new RawLog(); // have to fix it later, get from database
        // TODO: 2017/6/17 validate rawLog owner
        NormalLog normalLog = rawLogService.normalize(rawLog, form.toLogConfiguration());
        if (normalLog == null) {

        }
        return normalLog;
    }

    public void getLogByUserId(){}

    public void setLogState(int state){}

    public void getSharedLog(){}

    public void getLogByFuzzyName(){}  //模糊搜索

}
