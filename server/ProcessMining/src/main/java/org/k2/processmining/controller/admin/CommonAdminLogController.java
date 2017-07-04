package org.k2.processmining.controller.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.service.CommonLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/7/3.
 */
@Validated
public abstract class CommonAdminLogController<T extends AbstractLog> {

    private CommonLogService<T> logService;

    public CommonAdminLogController(CommonLogService<T> logService) {
        this.logService = logService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllRawLogs(@RequestParam("page") int page) {
        int pageNum = logService.getLogPageNum();
        if (page > pageNum) {
            page = pageNum;
        }
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getLogGroups(page);
        Map<String, Object> map = new HashMap<>();
        map.put("logGroups", logGroups);
        map.put("pageNum", pageNum);
        map.put("currPage", page);
        return map;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    Object getRawLogsByKeyWord(@Valid @RequestParam("keyWord")
                               @NotBlank(message = "Key word should not be empty.")String keyWord,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        int pageNum = logService.getLogPageNumByKeyWord(keyWord);
        if (page > pageNum) page = pageNum;
        List<LogGroup> logGroups = page == 0 ? Collections.emptyList() : logService.getLogGroupsByKeyWord(keyWord, page);
        Map<String,Object> res = new HashMap<>();
        res.put("logGroups",logGroups);
        res.put("pageNum", pageNum);
        res.put("currPage", page);
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteRawLogs(@Valid @RequestBody IdListForm form) {
        logService.deleteByAdmin(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }
}
