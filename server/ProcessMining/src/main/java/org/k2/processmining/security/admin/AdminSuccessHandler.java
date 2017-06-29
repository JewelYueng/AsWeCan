package org.k2.processmining.security.admin;

import org.k2.processmining.utils.GsonParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/28.
 */
public class AdminSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String ADMIN_LOGIN_SUCCESS = "admin login success";
    public static final int ADMIN_LOGIN_SUCCESS_CODE = 200;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth
    )throws IOException, ServletException {

        System.out.println("AdminSuccessHandler");
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            response.getWriter().print(GsonParser.parseToCodeAndMessage(ADMIN_LOGIN_SUCCESS_CODE,ADMIN_LOGIN_SUCCESS));
            response.getWriter().flush();
        } else {
            super.onAuthenticationSuccess(request, response, auth);
        }
    }
}