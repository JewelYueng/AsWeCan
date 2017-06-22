package org.k2.processmining.controller;

import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aria on 2017/6/13.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    public void register(){}

    public void deleteUser(){}

    public void changePassword(){}

    public void getUserInfo(){}

    public void activeAccount(){} //邮件激活

    public void setUserState(){}

    @RequestMapping(value = "",method = RequestMethod.GET)
    public @ResponseBody
    Object getAllUsers(){
        return null;
    }

    @RequestMapping(value = "/active",method = RequestMethod.POST)
    public @ResponseBody
    Object activeUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();

        userService.updateStateByUserId(idListForm.getIdList(), UserState.ACTIVE.getValue());
        map.put("code",1);
        return map;
    }

    @RequestMapping(value = "/freeze",method = RequestMethod.POST)
    public @ResponseBody
    Object freezeUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();
        userService.updateStateByUserId(idListForm.getIdList(),UserState.FREEZE.getValue());
        map.put("code",1);
        return map;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody
    Object register(@RequestBody User user){
        Map map = new HashMap();
//        userService.addUser();
        userService.addUser(user);
        return map;
    }
}
