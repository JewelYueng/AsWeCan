package org.k2.processmining.controller;

import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.admin.AdminDetails;
import org.k2.processmining.security.admin.AdminFailureHandler;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @RequestMapping(value = "/getAdmin",method = RequestMethod.GET)
    public @ResponseBody
    Object getAdmin(){
        Map<String,Object> map = new HashMap<String,Object>();
        Administrator administrator = getLoginAdmin();
        if (administrator == null){
            map.put("code","404");
        }else {
            map.put("code","200");
        }
        map.put("admin",administrator);
        return map;
    }

    @RequestMapping(value = "/home")
    public void home(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/admin_index.html").forward(request,response);
    }

    @RequestMapping(value = "/loginPage")
    public void loginPage(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
//        request.getRequestDispatcher("/html/admin_login.html").forward(request,response);
        request.getRequestDispatcher("/html/admin.html").forward(request,response);
    }

    @RequestMapping(value = "/accessDeniedPage")
    public void accessDeniedPage(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        System.out.println("admin AccessDenied");
        request.getRequestDispatcher("/html/admin_403.html").forward(request,response);
    }


    private Administrator getLoginAdmin(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AdminDetails){
            return ((AdminDetails)principal).getAdmin();
        }
        return null;
    }

}
