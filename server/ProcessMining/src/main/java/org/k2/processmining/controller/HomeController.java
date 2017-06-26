package org.k2.processmining.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/13.
 */

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    /**
     * 用户的登录页
     * @param request
     * @param response
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public void homeForUser(@RequestParam(value = "error",required = false) String error, HttpServletRequest request, HttpServletResponse response){

        try {
            if (error != null){
                System.out.println("这里是error不等于null时候的:"+error);
                request.getRequestDispatcher("/html/login_failure.html").forward(request,response);
            }
            else{
                System.out.println("error:"+error);
                request.getRequestDispatcher("/html/login.html").forward(request,response);
            }

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public void homeForAdmin(HttpServletRequest request,HttpServletResponse response){
        System.out.println("admin");
        try {
            request.getRequestDispatcher("/html/admin.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//管理员的主页

    @RequestMapping(value = "",method = RequestMethod.GET)
    public void homePage(HttpServletRequest request,HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/home.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/handleLogin",method = RequestMethod.POST)
    public String handleLogin(HttpServletRequest request,HttpServletResponse response){

        System.out.println("handleLogin");
        System.out.println("request.email:"+request.getParameter("username"));
        System.out.println("request.password:"+request.getParameter("password"));
        System.out.println("request.type:"+request.getParameter("type"));
//        try {
//            request.getRequestDispatcher("/login").forward(request,response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "login";
    }
}
