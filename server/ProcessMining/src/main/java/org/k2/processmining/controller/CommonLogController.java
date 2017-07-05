package org.k2.processmining.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.LogShareState;
import org.k2.processmining.model.LogState;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.CommonLogService;
import org.k2.processmining.storage.LogStorage;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by nyq on 2017/7/3.
 */
@Validated
public abstract class CommonLogController<T extends AbstractLog> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonLogController.class);

    private CommonLogService<T> logService;

    private LogStorage logStorage;

    private String logType;

    public CommonLogController(String logType, CommonLogService<T> logService, LogStorage logStorage) {
        this.logType = logType;
        this.logService = logService;
        this.logStorage = logStorage;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public@ResponseBody
    Object upload(@RequestParam("format") @NotBlank(message = "Format should not be empty.") String format,
                  @RequestParam(value = "isShare", defaultValue = "0") int isShare,
                  @RequestParam("file") @NotNull(message = "File should not be empty.") CommonsMultipartFile file) {
        User user = Util.getLoginUser();
        if (!LogShareState.isValid(isShare)) {
            isShare = LogShareState.UNSHARED.getValue();
        }
        T log = createLog(Util.getUUIDString(), user.getId(), format, new Date(),
                file.getOriginalFilename(), isShare);
        Map<String, Object> res = new HashMap<>();
        try (InputStream inputStream = file.getInputStream()){
            logService.save(log, inputStream);
        }
        catch (IOException e) {
            LOGGER.error("Fail to save log:", e);
            throw new InternalServerErrorException(Message.UPLOAD_FAIL);
        }
        res.put("code", 1);
        res.put(logType, log);
        return res;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@NotBlank(message = "The log id is invalid.") @RequestParam("id")String id,
                         HttpServletResponse response) {
        T log = logService.getLogById(id);
        try {
            logStorage.download(log, inputStream -> {
                String fileName = log.getLogName();
                response.setHeader("Content-Disposition","attachment;filename=" + Util.encodeForURL(fileName));
                IOUtils.copyLarge(inputStream, response.getOutputStream());
                return true;
            });
        }
        catch (IOException e) {
            LOGGER.error("Fail to download log:", e);
            throw new InternalServerErrorException(Message.DOWNLOAD_FAIL);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getLogsByUserId(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        User user = Util.getLoginUser();
        int pageNum = logService.getLogPageNumByUserId(user.getId());
        if (page > pageNum) {
            page = pageNum;
        }
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getLogsByUser(user, page);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        map.put("pageNum", pageNum);
        map.put("currPage", page);
        return map;
    }

    @RequestMapping(value = "/sharedLogs", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLog(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        int pageNum = logService.getSharedLogPageNum();
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getSharedLogs(page);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        map.put("pageNum", pageNum);
        map.put("currPage", page);
        return map;
    }


    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody
    Object shareLogs(@Valid @RequestBody IdListForm form){
        User user = Util.getLoginUser();
        logService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.SHARED.getValue(), user.getId());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

    @RequestMapping(value = "/unShare",method = RequestMethod.POST)
    public @ResponseBody
    Object unShareRawLogs(@Valid @RequestBody IdListForm form){
        User user = Util.getLoginUser();
        logService.updateShareStateByLogIdForUser(form.getIdList(), LogShareState.UNSHARED.getValue(), user.getId());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteByLogId(@Valid @RequestBody IdListForm form){
        Map<String, Object> res = new HashMap<>();
        User user = Util.getLoginUser();
        logService.updateStateByLogIdForUser(form.getIdList(), LogState.DELETE.getValue(), user.getId());
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public @ResponseBody
    Object getLogByKeyWord(@NotBlank(message = "The key word is invalid.") @RequestParam("keyWord") String keyWord,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        User user = Util.getLoginUser();
        int pageNum = logService.getLogPageNumByUserIdAndKeyWord(user.getId(), keyWord);
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getLogsByUserAndKeyWord(user, keyWord, page);
        Map<String,Object> res = new HashMap<>();
        res.put("logGroups",logGroups);
        res.put("pageNum", pageNum);
        res.put("currPage", page);
        return res;
    }

    @RequestMapping(value = "/sharedLogs/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getSharedLogByKeyWord(@Valid @RequestParam("keyWord")
                                   @NotBlank(message = "Key word should not be empty.") String keyWord,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        int pageNum = logService.getSharedLogPageNumByKeyWord(keyWord);
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getSharedLogsByKeyWord(keyWord, page);
        Map<String,Object> res = new HashMap<>();
        res.put("logGroups",logGroups);
        res.put("pageNum", pageNum);
        res.put("currPage", page);
        return res;
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public @ResponseBody
    Object getPageOfLogId(@NotBlank(message = "The logId should not be empty.")@RequestParam("id") String id) {
        User user = Util.getLoginUser();
        int page = logService.getPageOfLogId(user.getId(), id);
        return new HashMap<String,Object>(){{put("page", page);}};
    }

    @RequestMapping(value = "/sharedLogs/page")
    public @ResponseBody
    Object getPageOfSharedLogId(@NotBlank(message = "The logId should not be empty.")@RequestParam("id") String id) {
        int page = logService.getPageOfSharedLogId(id);
        return new HashMap<String,Object>(){{put("page", page);}};
    }
    public abstract T createLog(String id, String userId, String format,
                                Date createDate, String logName, int isShare);
}
