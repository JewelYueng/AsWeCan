package org.k2.processmining.security.admin;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Aria on 2017/6/28.
 */
public class AdmindeniedHandler implements AccessDeniedHandler{
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        HttpServletRequest httpRequest = httpServletRequest;
        // is ajax request?
        /**
         * 访问页面无权限时候拦截下来
         */
        System.out.println("AdmindeniedHandler");
        if ("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"))) {
            String msg = "{\"success\" : false, \"message\" : \"authentication-failure\"}";

            httpServletResponse.setContentType("json");
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(msg.getBytes());
            outputStream.flush();
        }
    }
}
