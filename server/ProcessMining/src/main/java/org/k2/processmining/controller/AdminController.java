package org.k2.processmining.controller;

import org.k2.processmining.mapper.AdminMapper;
import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aria on 2017/6/13.
 */

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/checkout")
    public @ResponseBody
    Object checkout(@RequestBody Administrator administrator){
        Map map = new HashMap();
        map.put("code",adminService.checkoutAdminByWorkIdAndPwd(administrator.getWorkId(),administrator.getPassword()));
        return map;
    }

    @RequestMapping(value = "")
    public @ResponseBody
    Object listAllAdmins(){
        Map map = new HashMap();
        map.put("fjaklfj","fa");
//        map.put("");
        return map;
    }

    @RequestMapping(value = "/login")
    public void adminLogin(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/admin.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
