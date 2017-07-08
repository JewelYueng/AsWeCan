package org.k2.processmining.controller.admin;

import org.k2.processmining.controller.IdListForm;
import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by Aria on 2017/7/3.
 */

@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    @Autowired
    UserService userService;

    @Autowired
    private SessionRegistryImpl sessionRegistry;

    @RequestMapping(value = "/active",method = RequestMethod.POST)
    public @ResponseBody
    Object activeUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();
        userService.updateStateByUserId(idListForm.getIdList(), UserState.ACTIVE.getValue());
        expireUser(new HashSet<>(idListForm.getIdList()));
        map.put("code",200);
        return map;
    }

    @RequestMapping(value = "/forbid",method = RequestMethod.POST)
    public @ResponseBody
    Object forbidUser(@Valid @RequestBody IdListForm idListForm){
        Map map = new HashMap();
        userService.updateStateByUserId(idListForm.getIdList(),UserState.FORBIDDEN.getValue());
        expireUser(new HashSet<>(idListForm.getIdList()));
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

    private void expireUser(Set<String> ids) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof MyUserDetails) {
                User user = ((MyUserDetails) principal).getUser();
                String id = user.getId();
                if (ids.contains(id)) {
                    List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation sessionInformation :sessionInformationList) {
                        sessionInformation.expireNow();
                    }
                }
            }
        }
    }
}
