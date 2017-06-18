package org.k2.processmining.controller;

import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping("/rawLog")
public class RawLogController {

    @Autowired
    private RawLogService rawLogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public@ResponseBody
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

    public void download() {}

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

    public void getLogByUserId(){}

    public void setLogState(int state){}

    public void getSharedLog(){}

    public void getLogByFuzzyName(){}  //模糊搜索

}
