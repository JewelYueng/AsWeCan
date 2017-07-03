package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogService;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/eventLog")
public class EventLogController extends CommonLogController<EventLog> {

    private static Logger LOGGER = LoggerFactory.getLogger(EventLogController.class);

    public static final String logType = "eventLog";

    private EventLogService logService;

    @Autowired
    public EventLogController(EventLogService logService, LogStorage logStorage) {
        super(logType, logService, logStorage);
        this.logService = logService;
    }

    @Override
    public EventLog createLog(String id, String userId, String format, Date createDate, String logName, int isShare) {
        EventLog eventLog = new EventLog();
        eventLog.setId(id);
        eventLog.setUserId(userId);
        eventLog.setFormat(format);
        eventLog.setCreateDate(new Date());
        eventLog.setLogName(logName);
        eventLog.setIsShared(isShare);
        return eventLog;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") @NotBlank(message = "Format should not be empty.") String format,
                  @RequestParam(value = "isShare", defaultValue = "0") int isShare,
                  @RequestParam("file") @NotNull(message = "File should not be empty.") CommonsMultipartFile file) {

        User user = getUser();
        if (!LogShareState.isValid(isShare)) {
            isShare = LogShareState.UNSHARED.getValue();
        }
        EventLog log = createLog(Util.getUUIDString(), user.getId(), format, new Date(),
                file.getOriginalFilename(), isShare);
        Map<String, Object> res = new HashMap<>();
        try (InputStream inputForRemote = file.getInputStream(); InputStream inputForSummarize = file.getInputStream()){
            logService.save(log, inputForRemote, inputForSummarize);
        }
        catch (IOException e) {
            LOGGER.error("Fail to save log:", e);
            throw new InternalServerErrorException(Message.UPLOAD_FAIL);
        }
        res.put("code", 1);
        res.put(logType, log);
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