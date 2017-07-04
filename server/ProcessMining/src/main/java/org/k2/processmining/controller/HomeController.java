package org.k2.processmining.controller;

import org.k2.processmining.exception.ForbiddenException;
import org.k2.processmining.utils.VerifyCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

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
    homeForUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/login.html").forward(request,response);
    }


    @RequestMapping(value = "",method = RequestMethod.GET)
    public void homePage(HttpServletRequest request,HttpServletResponse response){
        try {
            request.getRequestDispatcher("/html/index.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/code",method = RequestMethod.GET)
    public void getCode(@RequestParam(value = "date",required = false)String date,HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("getValidateCode");
        if (date != null){
            System.out.println(date);
        }else {
            System.out.println("date is null"+date);
        }
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

    @RequestMapping(value = "/accessDeniedJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void accessDeniedJSON() {
        throw new ForbiddenException("Attempted to access the protected resource!");
    }

//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public void testLogin(@RequestBody Map map){
//        map.forEach(new BiConsumer() {
//            @Override
//            public void accept(Object o, Object o2) {
//                System.out.println("o:"+o+"   o2:"+o2);
//            }
//        });
//    }

}
