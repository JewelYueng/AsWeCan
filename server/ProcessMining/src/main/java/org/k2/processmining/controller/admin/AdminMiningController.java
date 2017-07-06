package org.k2.processmining.controller.admin;

import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.controller.annotion.MethodJars;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.MethodState;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.k2.processmining.service.MiningMethodService;
import org.k2.processmining.support.algorithm.LoadMethodException;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/28.
 */
@Controller
@Validated
@RequestMapping("/admin/mining")
public class AdminMiningController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminMiningController.class);

    @Autowired
    private MiningMethodService miningMethodService;

    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public @ResponseBody
    Object getAllMethods() {
        Map<String, Object> res = new HashMap<>();
        List<Object> methodsConfigs = new LinkedList<>();
        List<MiningMethod> methods = miningMethodService.getAllMethods();
        for (MiningMethod method : methods) {
            Map<String, Object> configs = miningMethodService.getMethodConfig(method);
            if (configs != null && configs.size() > 0) {
                configs.put("state", method.getState());
                configs.put("id", method.getId());
                methodsConfigs.add(configs);
            }
        }
        res.put("methods", methodsConfigs);
        return res;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    Object addMiningMethod(@MethodJars @RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BadRequestException("At least one file!");
        }
        Map<String,Object> res = new HashMap<>();
        MiningMethod miningMethod = new MiningMethod();
        miningMethod.setId(Util.getUUIDString());
        try {
            miningMethodService.addMethod(miningMethod, files);
            res.put("state", miningMethod.getState());
            res.put("id", miningMethod.getId());
            res.put("configs", miningMethodService.getMethodConfig(miningMethod));
            return res;
        }
        catch (IOException e) {
            LOGGER.error("fail to create mergeMethod:", e);
            throw  new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        catch (LoadMethodException e) {
            LOGGER.error("fail to create mergeMethod: {}", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public @ResponseBody
    Object active(@Valid @RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.ACTIVE.getValue());
    }

    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    public @ResponseBody
    Object freeze(@Valid @RequestBody IdListForm form) {
        return setMethodState(form.getIdList(), MethodState.FREEZE.getValue());
    }

    private Map<String,Object> setMethodState(List<String> ids, int state) {
        miningMethodService.setMethodState(ids, state);
        Map<String,Object> res = new HashMap<>();
        res.put("code", 1);
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@Valid @RequestBody IdListForm form) {
        miningMethodService.delete(form.getIdList());
        return new HashMap<String,Object>(){{put("code", 1);}};
    }
}
