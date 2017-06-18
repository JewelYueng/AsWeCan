package org.k2.processmining.controller;

import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.support.event.transform.TransToEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/normalLog")
public class NormalLogController {

    @Autowired
    private NormalLogService normalLogService;

    public void getAllLogs(){}

    public void upload(){}

    public void download(){}

    public void deleteLogById(){}

    public void getLogByUserId(){}

    @RequestMapping(value = "/toEventLog", method = RequestMethod.POST)
    public @ResponseBody
    EventLog transToEvent(@RequestParam("id") String id) {
        NormalLog normalLog = new NormalLog(); // have to get from database
        // TODO: 2017/6/17 validate
        normalLog.setUserId("1");
        normalLog.setId(id);
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        if (eventLog != null) {
            return eventLog;
        }
        // add other msg
        return null;
    }

    public void setLogState(){}

    public void getSharedLog(){}

    public void getLogByFuzzyName(){}

}
