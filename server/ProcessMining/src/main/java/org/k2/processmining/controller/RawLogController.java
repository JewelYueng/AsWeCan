package org.k2.processmining.controller;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.GsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/rawLog")
public class RawLogController {

    @Autowired
    private RawLogService rawLogService;

    @Autowired
    private LogStorage logStorage;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") String format,
                  @RequestParam("isShare") int isShare,
                  @RequestParam("file")CommonsMultipartFile file) {
        String rawLogId = Util.getUUIDString();

        User user = getUser();

        if (!LogShareState.isValid(isShare)) {
            isShare = LogShareState.UNSHARED.getValue();
        }
        RawLog rawLog = new RawLog();
        rawLog.setId(rawLogId);
        rawLog.setUserId(user.getId());
        rawLog.setFormat(format);
        rawLog.setCreateDate(new Date());
        rawLog.setLogName(file.getOriginalFilename());
        rawLog.setIsShared(isShare);
        Map<String, Object> res = new HashMap<>();
        int code = 1;
        try (InputStream inputStream = file.getInputStream()){
            if (! rawLogService.save(rawLog, inputStream)) {
                code = 0;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            code = 0;
        }
        // may return rawLog
        res.put("code", code);
        return res;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        RawLog rawLog = rawLogService.getRawLogById(id);
        User user = getUser();
        if (!Util.isActiveAndBelongTo(rawLog, user) && !Util.isActiveAndShared(rawLog)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String fileName = rawLog.getLogName();
        response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
        try (OutputStream outputStream = response.getOutputStream()){
            logStorage.download(rawLog, outputStream);
            outputStream.flush();
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/normalize", method = RequestMethod.POST)
    public @ResponseBody
    Object normalize(@Valid @RequestBody NormalizeRawLogConfigForm form) {
        RawLog rawLog = rawLogService.getRawLogById(form.getRawLogId());
        User user = getUser();
        Map<String, Object> res = new HashMap<>();
        if (rawLog == null || rawLog.getUserId() == null
                || !rawLog.getUserId().equals(user.getId()) || !LogState.isActive(rawLog.getState())) {
            res.put("code", 0);
            res.put("msg", "The log is not exist!");
            return ResponseEntity.badRequest().body(res);
        }
        NormalLog normalLog = rawLogService.normalize(rawLog, form.toLogConfiguration());
        if (normalLog == null) {
            res.put("code", 0);
            return res;
        }
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogsByUserId() {
        User user = getUser();
        List<LogGroup> logGroups = rawLogService.getLogsByUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog() {
        List<LogGroup> logGroups = rawLogService.getSharedLogs();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }


    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody
    Object shareLogs(@Valid @RequestBody IdListForm form){
        User user = getUser();
        rawLogService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.SHARED.getValue(), user.getId());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }


    /**
     * 取消分享规范化日志
     * @param form
     * @return
     */
    @RequestMapping(value = "/unShare",method = RequestMethod.POST)
    public @ResponseBody
    Object unShareRawLogs(@RequestBody IdListForm form){
        User user = getUser();
        rawLogService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.UNSHARED.getValue(), user.getId());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

    /**
     * 删除日志
     * @param form
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteByLogId(@RequestBody IdListForm form){
        Map<String, Object> res = new HashMap<>();
        User user = getUser();
        rawLogService.updateStateByLogIdForUser(form.getIdList(), LogState.DELETE.getValue(), user.getId());
        res.put("code", 1);
        return res;
    }

    /**
     * 日志搜索
     * @param keyWord
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByFuzzyName(@RequestParam("keyWord")String keyWord){
        Map<String,Object> result = new HashMap<>();
        User user = getUser();
        List<LogGroup> logGroups = rawLogService.getLogByFuzzyName(keyWord,user);
        result.put("logGroups",logGroups);
        return result;
    }

    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByFuzzyName(@RequestParam("keyWord") String keyWord) {
        List<LogGroup> logGroups = rawLogService.getSharedLogsByFuzzyName(keyWord);
        return new HashMap<String,Object>(){{put("logGroups", logGroups);}};
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }
}
