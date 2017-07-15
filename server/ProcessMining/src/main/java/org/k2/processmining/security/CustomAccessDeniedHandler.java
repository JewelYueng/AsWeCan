package org.k2.processmining.security;

import org.k2.processmining.util.Message;
import org.k2.processmining.utils.GsonParser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/7/6.
 */
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        if ("application/json".equals(request.getHeader("Content-Type"))){
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.ACCESSDENIED_CODE,Message.ACCESSDENIED));
            response.setStatus(403);
        }else
            super.handle(request, response,e);
    }
}
