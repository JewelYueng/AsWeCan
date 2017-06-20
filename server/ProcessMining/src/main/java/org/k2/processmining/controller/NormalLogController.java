package org.k2.processmining.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.NormalLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Util;
import org.k2.processmining.utils.GsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private NormalLogService normalLogService;

    @Autowired
    private LogStorage logStorage;

    /**
     * 获取用户的规范化日志列表
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByUserId(HttpServletRequest request, HttpServletResponse response){
        User user = getUser();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", normalLogService.getLogGroupsByUserId(user.getId()));
        return map;
    }


    /**
     * 获取分享规范化日志列表
     * @return
     */
    @RequestMapping(value = "/sharedLogs",method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog(){
        return new HashMap<String, Object>() {{put("logGroups", normalLogService.getSharedLogGroups());}};
    }


    /**
     * 上传规范化日志
     * @param format
     * @param isShare
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public @ResponseBody
    Object uploadLog(@RequestParam("format") String format,
                     @RequestParam("isShare") int isShare,
                     @RequestParam("file")CommonsMultipartFile file){
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
        int code = 1;
        try (InputStream inputStream = file.getInputStream()){
            if (! normalLogService.save(normalLog, inputStream)) {
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


    /**
     * 下载规范化日志
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        NormalLog normalLog = normalLogService.getNormalLogById(id);
        User user = getUser();
        if (!Util.isActiveAndBelongTo(normalLog, user)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String fileName = normalLog.getLogName();
        response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
        try (OutputStream outputStream = response.getOutputStream()){
            logStorage.download(normalLog, outputStream);
            outputStream.flush();
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 分享规范化日志
     * @param map
     * @return
     */
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody
    Map shareNormalLogs(@RequestBody Map map){

        Map result = new HashMap();
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0 ){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.updateShareStateByLogId(idList,LogState.UNSHARED.getValue()));
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
    Map unShareNormalLogs(@RequestBody Map map){
        Map result = new HashMap();
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.updateShareStateByLogId(idList,LogState.UNSHARED.getValue()));
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
    Map deleteByLogId(@RequestBody Map map){
        Map result = new HashMap();
        System.out.println("map:"+map.get("idList").toString());
        List<String> idList = GsonParser.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.updateStateByLogId(idList,LogState.DELETE.getValue()));
        }
        return result;
    }


    /**
     * 搜索日志
     * @return
     */
    @RequestMapping(value = "/search")
    public @ResponseBody
    Object getLogByFuzzyName(@RequestParam("keyWord")String keyWord){
        Map<String,Object> result = new HashMap<>();
        normalLogService.getLogByFuzzyName(keyWord);
        return null;
    }


    /**
     * 转化为事件日志
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
            res.put("code", 0);
            return res;
        }
        res.put("code", 1);
        return res;
    }




    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
    }



    /**
     * 测试用
     * @return
     */
    @RequestMapping(value = "/testNormal")
    public @ResponseBody
    Map addNormalLog(){
        Map result = new HashMap();
        String id = UUID.randomUUID().toString();
        NormalLog normalLog = new NormalLog();
        normalLog.setId(id);
        normalLog.setUserId("0000000000000001");
        normalLog.setFormat("txt");
        normalLog.setCreateDate(new Date());
        normalLog.setLogName("testNormalLog");
        normalLogService.addNormalLog(normalLog);
        return null;
    }}
