package org.k2.processmining.controller;

import com.sun.deploy.net.HttpResponse;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
@RequestMapping("/rawLog")
public class RawLogController {

    @Autowired
    private RawLogService rawLogService;

    @Autowired
    private LogStorage logStorage;

    Object getAllLogs() {
        return null;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") String format,
                  @RequestParam("isShare") int isShare,
                  @RequestParam("file")CommonsMultipartFile file) {
        String rawLogId = UUID.randomUUID().toString();
        RawLog rawLog = new RawLog();
        rawLog.setId(rawLogId);
        rawLog.setUserId("1");
        rawLog.setFormat(format);
        rawLog.setCreateDate(new Date());
        rawLog.setLogName(file.getName());
        rawLog.setState(isShare);
        try (InputStream inputStream = file.getInputStream()){
            if (rawLogService.save(rawLog, inputStream)) {
                return rawLog;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        RawLog rawLog = new RawLog();
        rawLog.setUserId("1");
        rawLog.setId(id);
        rawLog.setLogName("rawLog.txt");
        String fileName = rawLog.getLogName();
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        try (OutputStream outputStream = response.getOutputStream()){
            logStorage.download(rawLog, outputStream);
            outputStream.flush();
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteLogById(List<Integer> idList){}

    @RequestMapping(value = "/normalize", method = RequestMethod.POST)
    public @ResponseBody
    NormalLog normalize(@Valid @RequestBody NormalizeRawLogConfigForm form) {
        RawLog rawLog = new RawLog(); // have to fix it later, get from database
        // TODO: 2017/6/17 validate rawLog owner
        rawLog.setId(form.getRawLogId());
        rawLog.setUserId("1");
        NormalLog normalLog = rawLogService.normalize(rawLog, form.toLogConfiguration());
        if (normalLog == null) {

        }
        return normalLog;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByUserId() {
        User user = new User();
        user.setId("1");
        List<LogGroup> logGroups = rawLogService.getLogsByUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    public void setLogState(int state){}

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog() {
        List<LogGroup> logGroups = rawLogService.getSharedLogs();
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        return map;
    }

    public void getLogByFuzzyName(){}  //模糊搜索

}
