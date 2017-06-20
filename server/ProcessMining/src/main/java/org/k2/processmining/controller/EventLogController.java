package org.k2.processmining.controller;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.EventLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.GsonParser;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private LogStorage logStorage;

    public void getAllLogs(){}

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
                code = 0;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            code = 0;
        }
        // may return log
        res.put("code", code);
        return res;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        EventLog eventLog = eventLogService.getEventLogById(id);
        User user = getUser();
        if (!Util.isActiveAndBelongTo(eventLog, user)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String fileName = eventLog.getLogName();
        response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
        try (OutputStream outputStream = response.getOutputStream()){
            logStorage.download(eventLog, outputStream);
            outputStream.flush();
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteLogById(List<Integer> idList){}

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogsByUserId() {
        User user = getUser();
        List<LogGroup> logGroups = eventLogService.getLogsByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    public void setLogState(int state){}

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog() {
        List<LogGroup> logGroups = eventLogService.getSharedLogs();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    public void getLogByFuzzyName(){}

    public void getSubmarizeByLogName(String logName){}

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }

    /**
     * 分享规范化日志
     * @param map
     * @return
     */
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody
    Object shareEventLogs(@RequestBody Map map){

        Map result = new HashMap();
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0 ){
            result.put("code",0);
        }else {
            result.put("code",eventLogService.updateShareStateByLogId(idList,LogState.SHARED.getValue()));
        }
        return result;
    }


    /**
     * 取消分享规范化日志
     * @param map
     * @return
     */
    @RequestMapping(value = "/unShare",method = RequestMethod.POST)
    public @ResponseBody
    Object unShareEventLogs(@RequestBody Map map){
        Map result = new HashMap();
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",eventLogService.updateShareStateByLogId(idList,LogState.UNSHARED.getValue()));
        }
        return result;
    }

    /**
     * 删除日志
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public @ResponseBody
    Object deleteByLogId(@RequestBody Map map){
        Map result = new HashMap();
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",eventLogService.updateStateByLogId(idList,LogState.DELETE.getValue()));
        }
        return result;
    }

    /**
     * 搜索日志
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByFuzzyName(@RequestParam("keyWord")String keyWord){
        Map<String,Object> result = new HashMap<>();
        User user = new User();
        user.setId("0000000000000001");
        List<LogGroup> logGroups = eventLogService.getLogByFuzzyName(keyWord,user);
        result.put("logGroups",logGroups);
        return result;
    }
}
