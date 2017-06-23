package org.k2.processmining.controller;

import org.k2.processmining.model.testPojo;
import org.k2.processmining.security.MyUserDetails;
import org.k2.processmining.service.MyTestService;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/14.
 */

@Controller
public class TestController {

    @Autowired
    MyTestService testService;

    @RequestMapping(value = "/test")
    public void test(){
        System.out.println("test");
        testService.insert(new testPojo(1111,"id2300"));
    }

    @RequestMapping(value = "/loginPage",method = RequestMethod.GET)
    public void loginPage(@RequestParam(value = "error", required = false) String error, Model model,
                            HttpServletRequest request,HttpServletResponse response) {
        if (error != null) {

            try {
                request.getRequestDispatcher("/html/login_failure.html").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
         request.getRequestDispatcher("/html/login.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value={"/welcome","/"},method=RequestMethod.GET)
    public String welcome(HttpServletRequest request,HttpServletResponse response){

        try {
            request.getRequestDispatcher("/html/home.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "index";
    }


    @RequestMapping(value = "/myHome")
    public String home(HttpServletRequest request, HttpServletResponse response){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails){
            String name = ((UserDetails)principal).getUsername();
            System.out.println(name);
        }else if (principal instanceof UserDetails){
            System.out.println("userDetails");
        }
        return "login";
    }
}
