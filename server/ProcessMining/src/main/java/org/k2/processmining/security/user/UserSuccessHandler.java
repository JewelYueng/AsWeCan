package org.k2.processmining.security.user;

import org.k2.processmining.util.Message;
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
public class UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

//    public static final String USER_LOGIN_SUCCESS = "user login success";
//    public static final int USER_LOGIN_SUCCESS_CODE = 200;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("UserSuccessHandler");

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_LOGIN_SUCCESS_CODE,Message.USER_LOGIN_SUCCESS));
            response.getWriter().flush();
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
