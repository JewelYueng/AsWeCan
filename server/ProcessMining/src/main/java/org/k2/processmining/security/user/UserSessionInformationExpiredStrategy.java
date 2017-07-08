package org.k2.processmining.security.user;

import org.k2.processmining.util.Util;
import org.k2.processmining.utils.GsonParser;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/7/7.
 */
public class UserSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy{
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        HttpServletRequest request = sessionInformationExpiredEvent.getRequest();
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        Util.delCookie(response,Util.getCookie(request.getCookies(),"userRememberMe"));
        Util.delCookie(response,Util.getCookie(request.getCookies(),"JSESSIONID"));
        if ("application/json".equals(request.getHeader("Content-Type"))){
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.setStatus(444);
            response.getWriter().print(GsonParser.parseToCodeAndMessage("405","你的账号已经被踢出！"));
        }else {
            request.getRequestDispatcher("/html/login.html").forward(request,response);
        }
    }
}
