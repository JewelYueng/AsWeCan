package org.k2.processmining.security.user;

import org.k2.processmining.security.admin.AdminDetailService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/28.
 */
public class UserFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        System.out.println("UserFailureHandler");
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            if (exception.getMessage().equals(MyUserDetailService.USER_NOT_FOUND)){
                response.getWriter().print("{\"code\":\"401\",\"message\":\""+MyUserDetailService.USER_NOT_FOUND+"\"}");
            }
            else if (exception.getMessage().equals("validate code is null")){
                response.getWriter().print("{\"code\":\"403\",\"message\":\""+"user validateCode is null！"+"\"}");
            }
            else {
                System.out.println(exception.getMessage());
                response.getWriter().print("{\"code\":\"402\",\"message\":\""+"user password is wrong！"+"\"}");
            }
            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
