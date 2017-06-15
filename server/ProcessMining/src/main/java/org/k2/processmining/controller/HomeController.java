package org.k2.processmining.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "/user")
    public void homeForUser(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/home.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//用户的主页

    @RequestMapping(value = "/admin")
    public void homeForAdmin(HttpServletRequest request,HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/admin.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//管理员的主页
}
