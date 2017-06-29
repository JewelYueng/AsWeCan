package org.k2.processmining.controller.admin;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.exception.JSONBadRequestException;
import org.k2.processmining.exception.JSONInternalServerErrorException;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.k2.processmining.service.MergeMethodService;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/28.
 */

@Controller
@RequestMapping("/admin/merge")
public class AdminMergeController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminMergeController.class);

    @Autowired
    private MergeMethodService mergeMethodService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    Object addMergeMethod(@Param("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new JSONBadRequestException("At least one file!");
        }
        Map<String,Object> res = new HashMap<>();
        MergeMethod mergeMethod = new MergeMethod();
        mergeMethod.setId(Util.getUUIDString());
        try {
            mergeMethodService.addMethod(mergeMethod, files);
            res.put("state", mergeMethod.getState());
            res.put("id", mergeMethod.getId());
            res.put("configs", mergeMethodService.getMethodConfig(mergeMethod));
            return res;
        }
        catch (IOException e) {
            LOGGER.error("fail to create mergeMethod: {}", e);
            throw  new JSONInternalServerErrorException();
        }
        catch (LoadMethodException e) {
            LOGGER.error("fail to create mergeMethod: {}", e);
            throw new JSONBadRequestException(e.getMessage());
        }
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public @ResponseBody
    Object active(@RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.ACTIVE.getValue());
    }

    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    public @ResponseBody
    Object freeze(@RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.FREEZE.getValue());
    }

    private Map<String,Object> setMethodState(List<String> ids, int state) {
        mergeMethodService.setMethodState(ids, state);
        Map<String,Object> res = new HashMap<>();
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@RequestBody IdListForm form) {
        mergeMethodService.delete(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }
}