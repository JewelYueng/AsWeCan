package org.k2.processmining.controller;

import org.k2.processmining.util.Util;
import org.k2.processmining.utils.VerifyCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    @RequestMapping(value = "/loginPage",method = RequestMethod.GET)
    public void
    loginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/login.html").forward(request,response);
//        request.getRequestDispatcher("/html/exampleLogin.html").forward(request,response);
    }

    @RequestMapping(value = "/accessDeniedPage",method = RequestMethod.GET)
    public void accessDeniedPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/user_403.html").forward(request,response);
    }

    @RequestMapping(value = "/hasLogoutPage",method = RequestMethod.GET)
    public void hasLogoutPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = Util.getCookie(request.getCookies(),"userRememberMe");
        Util.delCookie(response,cookie);
        request.getRequestDispatcher("/html/user_403.html").forward(request,response);
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public void homePage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
            request.getRequestDispatcher("/html/index.html").forward(request,response);
//            request.getRequestDispatcher("/html/exampleHome.html").forward(request,response);
    }

    @RequestMapping(value = "/code",method = RequestMethod.GET)
    public void getCode(@RequestParam(value = "date",required = false)String date,HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("getValidateCode");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        //存入会话session
        HttpSession session = request.getSession(true);
        //删除以前的
        session.removeAttribute("validateCode");
        session.setAttribute("validateCode", verifyCode.toLowerCase());
        //生成图片
        int w = 100, h = 30;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);

    }


}
