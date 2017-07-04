package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogService;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/normalLog")
@Validated
public class NormalLogController extends CommonLogController<NormalLog> {

    private static Logger LOGGER = LoggerFactory.getLogger(NormalLogController.class);

    public static final String logType = "normalLog";

    @Autowired
    private NormalLogService normalLogService;

    @Autowired
    public NormalLogController(CommonLogService<NormalLog> logService, LogStorage logStorage) {
        super(logType, logService, logStorage);
    }

    @Override
    public NormalLog createLog(String id, String userId, String format, Date createDate, String logName, int isShare) {
        NormalLog normalLog = new NormalLog();
        normalLog.setId(id);
        normalLog.setUserId(userId);
        normalLog.setFormat(format);
        normalLog.setCreateDate(new Date());
        normalLog.setLogName(logName);
        normalLog.setIsShared(isShare);
        return normalLog;
    }

    @RequestMapping(value = "/toEventLog", method = RequestMethod.POST)
    public @ResponseBody
    Object transToEvent(@Valid @RequestParam("id")
                        @NotBlank(message = "The logId should not be empty.") String id) {
        NormalLog normalLog = normalLogService.getLogById(id);
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        if (eventLog == null) {
            throw new InternalServerErrorException(Message.TRANS_TO_EVENT_LOG_FAIL);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("code", 1);
        res.put("eventLog",eventLog);
        return res;
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }
}
