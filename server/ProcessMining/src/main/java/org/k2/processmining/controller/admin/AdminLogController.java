package org.k2.processmining.controller.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.service.RawLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * Created by nyq on 2017/7/3.
 */
@Controller
@Validated
@RequestMapping("/admin")
public class AdminLogController {

    @Autowired
    private RawLogService rawLogService;

    @Autowired
    private NormalLogService normalLogService;

    @Autowired
    private EventLogService eventLogService;

    @RequestMapping(value = "/rawLog", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllRawLogs() {
        return new HashMap<String,Object>(){{put("logGroups", rawLogService.getLogGroups());}};
    }

    @RequestMapping(value = "/rawLog/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getRawLogsByKeyWord(@Valid @RequestParam("keyWord")
                               @NotBlank(message = "Key word should not be empty.")String keyWord) {
        return new HashMap<String,Object>(){{put("logGroups", rawLogService.getLogGroupsByKeyWord(keyWord));}};
    }

    @RequestMapping(value = "/rawLog/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteRawLogs(@Valid @RequestBody IdListForm form) {
        rawLogService.deleteByAdmin(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

    @RequestMapping(value = "/normalLog", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllNormalLogs() {
        return new HashMap<String,Object>(){{put("logGroups", normalLogService.getLogGroups());}};
    }

    @RequestMapping(value = "/normalLog/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getNormalLogsByKeyWord(@Valid @RequestParam("keyWord")
                               @NotBlank(message = "Key word should not be empty.")String keyWord) {
        return new HashMap<String,Object>(){{put("logGroups", normalLogService.getLogGroupsByKeyWord(keyWord));}};
    }

    @RequestMapping(value = "/normalLog/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteNormalLogs(@Valid @RequestBody IdListForm form) {
        normalLogService.deleteByAdmin(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

    @RequestMapping(value = "/eventLog", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllEventLogs() {
        return new HashMap<String,Object>(){{put("logGroups", eventLogService.getLogGroups());}};
    }

    @RequestMapping(value = "/eventLog/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getEventLogsByKeyWord(@Valid @RequestParam("keyWord")
                               @NotBlank(message = "Key word should not be empty.")String keyWord) {
        return new HashMap<String,Object>(){{put("logGroups", eventLogService.getLogGroupsByKeyWord(keyWord));}};
    }

    @RequestMapping(value = "/eventLog/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteEventLogs(@Valid @RequestBody IdListForm form) {
        eventLogService.deleteByAdmin(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }


}
