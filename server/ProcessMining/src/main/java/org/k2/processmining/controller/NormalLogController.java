package org.k2.processmining.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/normalLog")
public class NormalLogController {

    @Autowired
    private NormalLogService normalLogService;

    /**
     * 获取用户的规范化日志列表
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public @ResponseBody
    List<NormalLog> getLogByUserId(HttpServletRequest request, HttpServletResponse response){
        String userId = null;
        if (request.getSession().getAttribute("userid")!=null){
            userId = request.getSession().getAttribute("userid").toString();
            //TODO
            //验证userid是否有效
        }

        return normalLogService.getNormalLogsByUserId(userId);
    }


    /**
     * 获取分享规范化日志列表
     * @return
     */
    @RequestMapping(value = "/sharedLogs",method = RequestMethod.GET)
    public @ResponseBody
    List<NormalLog> getSharedLog(){
        //TODO
        //验证session中的userid和username存在且有效后才能取数据

        return normalLogService.getAllSharedNormalLogs();
    }


    /**
     * 上传规范化日志
     * @param format
     * @param isShare
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadLog",method = RequestMethod.POST)
    Object uploadLog(@RequestParam("format") String format,
                     @RequestParam("isShare") int isShare,
                     @RequestParam("file")CommonsMultipartFile file){
        return null;
    }


    /**
     * 下载规范化日志
     * @return
     */
    @RequestMapping(value = "/download")
    Object download(){
        return null;
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
        Gson gson = new GsonBuilder().create();
        System.out.println("map.toString:"+map.get("idList").toString());
        List<String> idList = gson.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0 ){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.updateShareStateByLogId(idList,1));
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
        Gson gson = new GsonBuilder().create();
        List<String> idList = gson.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.updateShareStateByLogId(idList,0));
        }
        return result;
    }


    /**
     * 删除规范化日志
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public @ResponseBody
    Map deleteLogById(@RequestBody Map map){
        Map result = new HashMap();
        Gson gson = new GsonBuilder().create();
        List<String> idList = gson.fromJson(map.get("idList").toString(),ArrayList.class);
        if (idList.size() == 0){
            result.put("code",0);
        }else {
            result.put("code",normalLogService.deleteLogByLogId(idList));
        }

        return null;
    }


    /**
     * 搜索日志
     * @return
     */
    @RequestMapping(value = "/search")
    public @ResponseBody
    List<NormalLog> getLogByFuzzyName(){
        return null;
    }


    /**
     * 转化为事件日志
     * @param id
     * @return
     */
    @RequestMapping(value = "/toEventLog", method = RequestMethod.POST)
    public @ResponseBody
    EventLog transToEvent(@RequestParam("id") String id) {
        NormalLog normalLog = new NormalLog(); // have to get from database
        // TODO: 2017/6/17 validate
        normalLog.setUserId("1");
        normalLog.setId(id);
        EventLog eventLog = normalLogService.transToEventLog(normalLog);
        if (eventLog != null) {
            return eventLog;
        }
        // addNormalLog other msg
        return null;
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
