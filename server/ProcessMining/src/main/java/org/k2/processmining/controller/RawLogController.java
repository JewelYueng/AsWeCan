package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogService;
import org.k2.processmining.service.RawLogService;
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
@RequestMapping("/rawLog")
@Validated
public class RawLogController extends CommonLogController<RawLog>{

    @Autowired
    private RawLogService rawLogService;

    private static Logger LOGGER = LoggerFactory.getLogger(RawLogController.class);

    @Autowired
    public RawLogController( CommonLogService<RawLog> logService, LogStorage logStorage) {
        super("rawLog", logService, logStorage);
    }

    @Override
    public RawLog createLog(String id, String userId, String format, Date createDate, String logName, int isShare) {
        RawLog rawLog = new RawLog();
        rawLog.setId(id);
        rawLog.setUserId(userId);
        rawLog.setFormat(format);
        rawLog.setCreateDate(new Date());
        rawLog.setLogName(logName);
        rawLog.setIsShared(isShare);
        return rawLog;
    }

    @RequestMapping(value = "/normalize", method = RequestMethod.POST)
    public @ResponseBody
    Object normalize(@Valid @RequestBody NormalizeRawLogConfigForm form) {
        RawLog rawLog = rawLogService.getLogById(form.getRawLogId());
        Map<String, Object> res = new HashMap<>();
        NormalLog normalLog = rawLogService.normalize(rawLog, form.toLogConfiguration());
        if (normalLog == null) {
            throw new InternalServerErrorException(Message.NORMALIZE_FAIL);
        }
        res.put("code", 1);
        res.put("normalLog", normalLog);
        return res;
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
//        return ((IUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
