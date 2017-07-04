package org.k2.processmining.controller.admin;

import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.model.UserState;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aria on 2017/7/3.
 */

@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/active",method = RequestMethod.POST)
    public @ResponseBody
    Object activeUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();
        userService.updateStateByUserId(idListForm.getIdList(), UserState.ACTIVE.getValue());
        map.put("code",200);
        return map;
    }

    @RequestMapping(value = "/freeze",method = RequestMethod.POST)
    public @ResponseBody
    Object freezeUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();
        userService.updateStateByUserId(idListForm.getIdList(),UserState.FREEZE.getValue());
        map.put("code",200);
        return map;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@RequestBody IdListForm ids){
        Map map = new HashMap();
        userService.deleteUserById(ids.getIdList());
        map.put("code",200);
        return map;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public @ResponseBody
    Object getAllUsers(){
        Map map = new HashMap();
        map.put("users",userService.getAllUsers());
        return map;
    }
}
