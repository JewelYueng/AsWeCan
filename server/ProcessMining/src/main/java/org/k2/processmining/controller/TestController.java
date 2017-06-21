package org.k2.processmining.controller;

import org.k2.processmining.model.testPojo;
import org.k2.processmining.service.MyTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @RequestMapping(value = "/login")
//    public void   login(HttpServletRequest request,HttpServletResponse response){
//        try {
//            request.getRequestDispatcher("/html/admin.html").forward(request,response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @RequestMapping(value = "/myHome")
    public String home(HttpServletRequest request, HttpServletResponse response, Model model){
//        try {
//            request.getRequestDispatcher("/html/home.html").forward(request,response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return "login";
    }
}
