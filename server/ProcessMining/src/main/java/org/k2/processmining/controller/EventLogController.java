package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.k2.processmining.exception.JSONInternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/eventLog")
public class EventLogController {

    private static Logger LOGGER = LoggerFactory.getLogger(EventLogController.class);

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private LogStorage logStorage;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") String format,
                  @RequestParam("isShare") int isShare,
                  @RequestParam("file")CommonsMultipartFile file) {
        String eventLogId = Util.getUUIDString();

        User user = getUser();

        if (!LogShareState.isValid(isShare)) {
            isShare = LogShareState.UNSHARED.getValue();
        }
        EventLog eventLog = new EventLog();
        eventLog.setId(eventLogId);
        eventLog.setUserId(user.getId());
        eventLog.setFormat(format);
        eventLog.setCreateDate(new Date());
        eventLog.setLogName(file.getOriginalFilename());
        eventLog.setIsShared(isShare);
        Map<String, Object> res = new HashMap<>();
        int code = 1;
        try (InputStream inputForRemote = file.getInputStream(); InputStream inputForSummarize = file.getInputStream()){
            if (! eventLogService.save(eventLog, inputForRemote, inputForSummarize)) {
                throw new JSONInternalServerErrorException("上传失败，请稍后尝试！");
            }
        }
        catch (IOException e) {
            LOGGER.error("Fail to save eventLog: {}", e);
            throw new JSONInternalServerErrorException("上传失败，请稍后尝试！");
        }
        res.put("eventLog", eventLog);
        res.put("code", code);
        return res;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        EventLog eventLog = eventLogService.getEventLogById(id);
        Boolean isSuccess = logStorage.download(eventLog, inputStream -> {
            String fileName = eventLog.getLogName();
            response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
            try {
                IOUtils.copyLarge(inputStream, response.getOutputStream());
            }
            catch (IOException e) {
                LOGGER.error("Fail to download eventLog: {}", e);
                throw new JSONInternalServerErrorException();
            }
            return true;
        });
        if (isSuccess == null || !isSuccess) {
            LOGGER.error("Fail to download eventLog! Is the file<{}> exist in the file system?", id);
            throw new JSONInternalServerErrorException();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogsByUserId() {
        User user = getUser();
        List<LogGroup> logGroups = eventLogService.getLogsByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog() {
        List<LogGroup> logGroups = eventLogService.getSharedLogs();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }

    /**
     * 分享规范化日志
     * @param form
     * @return
     */
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody
    Object shareEventLogs(@RequestBody IdListForm form){
        User user = getUser();
        eventLogService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.SHARED.getValue(), user.getId());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }


    /**
     * 取消分享规范化日志
     * @param form
     * @return
     */
    @RequestMapping(value = "/unShare",method = RequestMethod.POST)
    public @ResponseBody
    Object unShareEventLogs(@RequestBody IdListForm form){
        Map<String,Object> result = new HashMap();
        User user = getUser();
        eventLogService.updateShareStateByLogIdForUser(form.getIdList(),LogShareState.UNSHARED.getValue(), user.getId());
        result.put("code", 1);
        return result;
    }

    /**
     * 删除日志
     * @param form
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteByLogId(@RequestBody IdListForm form){
        Map<String,Object> result = new HashMap<>();
        User user = getUser();
        eventLogService.updateStateByLogIdForUser(form.getIdList(),LogState.DELETE.getValue(), user.getId());
        result.put("code", 1);
        return result;
    }

    /**
     * 搜索日志
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByFuzzyName(@RequestParam("keyWord") String keyWord){
        Map<String,Object> result = new HashMap<>();
        User user = getUser();
        List<LogGroup> logGroups = eventLogService.getLogByFuzzyName(keyWord,user);
        result.put("logGroups",logGroups);
        return result;
    }

    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByFuzzyName(@RequestParam("keyWord") String keyWord) {
        List<LogGroup> logGroups = eventLogService.getSharedLogsByFuzzyName(keyWord);
        return new HashMap<String,Object>(){{put("logGroups", logGroups);}};
    }
}
