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
public class NormalLogController {

    private static Logger LOGGER = LoggerFactory.getLogger(NormalLogController.class);

    @Autowired
    private NormalLogService normalLogService;

    @Autowired
    private LogStorage logStorage;

    /**
     * 获取用户的规范化日志列表
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByUserId(HttpServletRequest request, HttpServletResponse response) {
        User user = getUser();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", normalLogService.getLogGroupsByUserId(user.getId()));
        return map;
    }


    /**
     * 获取分享规范化日志列表
     *
     * @return
     */
    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog() {
        return new HashMap<String, Object>() {{
            put("logGroups", normalLogService.getSharedLogGroups());
        }};
    }


    /**
     * 上传规范化日志
     *
     * @param format
     * @param isShare
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadLog(@RequestParam("format") @NotBlank(message = "Format should not be empty.") String format,
                     @RequestParam(value = "isShare", defaultValue = "0") int isShare,
                     @RequestParam("file") @NotNull(message = "File should not be empty.") CommonsMultipartFile file) {
        String normalLogId = Util.getUUIDString();

        User user = getUser();

        if (!LogShareState.isValid(isShare)) {
            isShare = LogShareState.UNSHARED.getValue();
        }
        NormalLog normalLog = new NormalLog();
        normalLog.setId(normalLogId);
        normalLog.setUserId(user.getId());
        normalLog.setFormat(format);
        normalLog.setCreateDate(new Date());
        normalLog.setLogName(file.getOriginalFilename());
        normalLog.setIsShared(isShare);
        Map<String, Object> res = new HashMap<>();
        try (InputStream inputStream = file.getInputStream()) {
            normalLogService.save(normalLog, inputStream);
        } catch (IOException e) {
            LOGGER.error("Fail to save normalLog:", e);
            throw new InternalServerErrorException(Message.UPLOAD_FAIL);
        }
        res.put("normalLog", normalLog);
        res.put("code", 1);
        return res;
    }


    /**
     * 下载规范化日志
     *
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") @NotBlank(message = "The logId is invalid.") String id,
                         HttpServletResponse response) {
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        try {
            logStorage.download(normalLog, inputStream -> {
                String fileName = normalLog.getLogName();
                response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
                IOUtils.copyLarge(inputStream, response.getOutputStream());
                return true;
            });
        }
        catch (IOException e) {
            LOGGER.error("Fail to download normalLog:", e);
            throw new InternalServerErrorException(Message.DOWNLOAD_FAIL);
        }
    }


    /**
     * 分享规范化日志
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public @ResponseBody
    Object shareLogs(@Valid @RequestBody IdListForm form) {
        Map<String, Object> res = new HashMap<>();
        User user = getUser();
        normalLogService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.SHARED.getValue(), user.getId());
        res.put("code", 1);
        return res;
    }


    /**
     * 取消分享规范化日志
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/unShare", method = RequestMethod.POST)
    public @ResponseBody
    Object unShareNormalLogs(@Valid @RequestBody IdListForm form) {
        Map<String, Object> res = new HashMap<>();
        User user = getUser();
        normalLogService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.UNSHARED.getValue(), user.getId());
        res.put("code", 1);
        return res;
    }

    /**
     * 删除日志
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteByLogId(@Valid @RequestBody IdListForm form) {
        Map<String, Object> result = new HashMap<>();
        User user = getUser();
        normalLogService.updateStateByLogIdForUser(form.getIdList(), LogState.DELETE.getValue(), user.getId());
        result.put("code", 1);
        return result;
    }


    /**
     * 搜索日志
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByFuzzyName(@Valid @RequestParam("keyWord")
                             @NotBlank(message = "Key word should not be empty.") String keyWord) {
        Map<String, Object> result = new HashMap<>();
        User user = getUser();
        List<LogGroup> logGroups = normalLogService.getLogByFuzzyName(keyWord, user);
        result.put("logGroups", logGroups);
        return result;
    }


    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByFuzzyName(@Valid @RequestParam("keyWord")
                                   @NotBlank(message = "Key word should not be empty.") String keyWord) {
        List<LogGroup> logGroups = normalLogService.getSharedLogsByFuzzyName(keyWord);
        return new HashMap<String,Object>(){{put("logGroups", logGroups);}};
    }

    /**
     * 转化为事件日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/toEventLog", method = RequestMethod.POST)
    public @ResponseBody
    Object transToEvent(@Valid @RequestParam("id")
                        @NotBlank(message = "EventLogId should not be empty.") String id) {
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        Map<String, Object> res = new HashMap<>();
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        if (eventLog == null) {
            throw new InternalServerErrorException(Message.TRANS_TO_EVENT_LOG_FAIL);
        }
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
