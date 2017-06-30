package org.k2.processmining.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/29.
 */
public class UserLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler{

    public static final String LOGOUT_SUCCESS = "user logout success";
    public static final int LOGOUt_SUCCESS_CODE = 200;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("UserLogoutSuccessHandler");

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            System.out.println("json");
            response.getWriter().print("{\"code\":"+LOGOUt_SUCCESS_CODE+",\"message\":\""+LOGOUT_SUCCESS+"\"}");
            response.getWriter().flush();
        } else {
            super.onLogoutSuccess(request, response, authentication);
        }

    }
}