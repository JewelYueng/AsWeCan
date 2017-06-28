package org.k2.processmining.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.k2.processmining.exception.JSONInternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.GsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/normalLog")
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
    Object uploadLog(@RequestParam("format") String format,
                     @RequestParam("isShare") int isShare,
                     @RequestParam("file") CommonsMultipartFile file) {
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
            if (!normalLogService.save(normalLog, inputStream)) {
                throw new JSONInternalServerErrorException("上传失败，请稍后尝试！");
            }
        } catch (IOException e) {
            LOGGER.error("Fail to save normalLog: {}", e);
            throw new JSONInternalServerErrorException("上传失败，请稍后尝试！");
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
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        logStorage.download(normalLog, inputStream -> {
            String fileName = normalLog.getLogName();
            response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
            try {
                IOUtils.copyLarge(inputStream, response.getOutputStream());
            }
            catch (IOException e) {
                LOGGER.error("Fail to download normalLog: {}", e);
                throw new JSONInternalServerErrorException();
            }
            return true;
        });
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
    Object unShareNormalLogs(@RequestBody IdListForm form) {
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
    Object deleteByLogId(@RequestBody IdListForm form) {
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
    Object getLogByFuzzyName(@RequestParam("keyWord") String keyWord) {
        System.out.println("keyWord:" + keyWord);
        Map<String, Object> result = new HashMap<>();
        User user = new User();
        user.setId("1");
        List<LogGroup> logGroups = normalLogService.getLogByFuzzyName(keyWord, user);
        result.put("logGroups", logGroups);
        return result;
    }


    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByFuzzyName(@RequestParam("keyWord") String keyWord) {
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
    Object transToEvent(@RequestParam("id") String id) {
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        User user = getUser();
        Map<String, Object> res = new HashMap<>();
        if (normalLog == null || normalLog.getUserId() == null
                || !normalLog.getUserId().equals(user.getId()) || !LogState.isActive(normalLog.getState())) {
            res.put("code", 0);
            res.put("msg", "The log is not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        if (eventLog == null) {
            throw new JSONInternalServerErrorException("转化为事件日志失败，请检查输入，稍后尝试！");
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
