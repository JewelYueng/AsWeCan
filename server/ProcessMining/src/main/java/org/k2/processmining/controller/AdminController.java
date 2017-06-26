package org.k2.processmining.controller;

import org.k2.processmining.mapper.AdminMapper;
import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.security.AdminLoginFailureHandler;
import org.k2.processmining.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public void adminLogin(@RequestParam(value = "error",required = false)String error, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (AdminLoginFailureHandler.ADMIN_NOT_FOUND.equals(error)){
            System.out.println("COntroller：用户不存在！");
            request.getRequestDispatcher("/html/admin_notfound.html").forward(request,response);
        }else if (AdminLoginFailureHandler.ADMIN_PWD_ERROR.equals(error)){
            System.out.println("Controller:密码错误！");
            request.getRequestDispatcher("/html/login_failure.html").forward(request,response);
        }else {
            request.getRequestDispatcher("/html/admin.html").forward(request,response);
        }

    }

    @RequestMapping(value = "/home")
    public void home(HttpServletRequest request,HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/activateSuccess.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
