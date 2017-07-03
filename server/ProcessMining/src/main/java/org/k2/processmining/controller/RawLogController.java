package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.RawLogService;
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
@RequestMapping("/rawLog")
@Validated
public class RawLogController {

    @Autowired
    private RawLogService rawLogService;

    @Autowired
    private LogStorage logStorage;

    private static Logger LOGGER = LoggerFactory.getLogger(RawLogController.class);

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") @NotBlank(message = "Format should not be empty.") String format,
                  @RequestParam(value = "isShare", defaultValue = "0") int isShare,
                  @RequestParam("file") @NotNull(message = "File should not be empty.") CommonsMultipartFile file) {
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
        try (InputStream inputStream = file.getInputStream()){
            rawLogService.save(rawLog, inputStream);
        }
        catch (IOException e) {
            LOGGER.error("Fail to save rawLog:", e);
            throw new InternalServerErrorException(Message.UPLOAD_FAIL);
        }
        res.put("code", 1);
        res.put("rawLog", rawLog);
        return res;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@NotBlank(message = "The log id is invalid.") @RequestParam("id")String id,
                         HttpServletResponse response) {
        RawLog rawLog = rawLogService.getRawLogById(id);
        try {
            logStorage.download(rawLog, inputStream -> {
                String fileName = rawLog.getLogName();
                response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
                IOUtils.copyLarge(inputStream, response.getOutputStream());
                return true;
            });
        }
        catch (IOException e) {
            LOGGER.error("Fail to download rawLog:", e);
            throw new InternalServerErrorException(Message.DOWNLOAD_FAIL);
        }
    }

    @RequestMapping(value = "/normalize", method = RequestMethod.POST)
    public @ResponseBody
    Object normalize(@Valid @RequestBody NormalizeRawLogConfigForm form) {
        RawLog rawLog = rawLogService.getRawLogById(form.getRawLogId());
        Map<String, Object> res = new HashMap<>();
        NormalLog normalLog = rawLogService.normalize(rawLog, form.toLogConfiguration());
        if (normalLog == null) {
            throw new InternalServerErrorException(Message.NORMALIZE_FAIL);
        }
        res.put("code", 1);
        res.put("normalLog", normalLog);
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogsByUserId(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        User user = getUser();
        int pageNum = rawLogService.getLogPageNumByUserId(user.getId());
        if (page > pageNum) {
            page = pageNum;
        }
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : rawLogService.getLogsByUser(user, page);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        map.put("pageNum", pageNum);
        map.put("currPage", page);
        return map;
    }

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        int pageNum = rawLogService.getSharedLogPageNum();
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : rawLogService.getSharedLogs(page);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        map.put("pageNum", pageNum);
        map.put("currPage", page);
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
    Object unShareRawLogs(@Valid @RequestBody IdListForm form){
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
    Object deleteByLogId(@Valid @RequestBody IdListForm form){
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
    Object getLogByFuzzyName(@NotBlank(message = "The key word is invalid.") @RequestParam("keyWord") String keyWord,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        User user = getUser();
        int pageNum = rawLogService.getLogPageNumByUserIdAndKeyWord(user.getId(), keyWord);
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : rawLogService.getLogsByUserAndKeyWord(user, keyWord, page);
        Map<String,Object> res = new HashMap<>();
        res.put("logGroups",logGroups);
        res.put("pageNum", pageNum);
        res.put("currPage", page);
        return res;
    }

    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByFuzzyName(@Valid @RequestParam("keyWord")
                                   @NotBlank(message = "Key word should not be empty.") String keyWord,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        int pageNum = rawLogService.getSharedLogPageNumByKeyWord(keyWord);
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : rawLogService.getSharedLogsByKeyWord(keyWord, page);
        Map<String,Object> res = new HashMap<>();
        res.put("logGroups",logGroups);
        res.put("pageNum", pageNum);
        res.put("currPage", page);
        return res;
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public @ResponseBody
    Object getPageOfLogId(@RequestParam("id") String id) {
        User user = getUser();
        int page = rawLogService.getPageOfLogId(user.getId(), id);
        return new HashMap<String,Object>(){{put("page", page);}};
    }

    @RequestMapping(value = "/sharedLogs/page")
    public @ResponseBody
    Object getPageOfSharedLogId(@RequestParam("id") String id) {
        int page = rawLogService.getPageOfSharedLogId(id);
        return new HashMap<String,Object>(){{put("page", page);}};
    }

    private User getUser() {
        User user = new User();
        user.setId("1");
        user.setName("y2k");
        return user;
//        return ((IUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
