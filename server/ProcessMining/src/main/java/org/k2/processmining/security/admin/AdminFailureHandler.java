package org.k2.processmining.security.admin;

import org.k2.processmining.util.Message;
import org.k2.processmining.utils.GsonParser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/28.
 */
public class AdminFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        System.out.println("AdminFailureHandler:"+exception.getMessage());
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            System.out.println(exception.getMessage());
            if (exception.getMessage().equals(Message.ADMIN_NOT_FOUND_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.ADMIN_NOT_FOUND_CODE,Message.ADMIN_NOT_FOUND));
            }
            else if (exception.getMessage().equals(Message.ADMIN_VALIDATECODE_NULL_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.ADMIN_VALIDATECODE_NULL_CODE,Message.ADMIN_VALIDATECODE_NULL));
            }else if (exception.getMessage().equals(Message.ADMIN_VALIDATECODE_WRONG_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.ADMIN_VALIDATECODE_WRONG_CODE,Message.ADMIN_VALIDATECODE_WRONG));
            }
            else {
                System.out.println(exception.getMessage());
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.ADMIN_PASSWORD_WRONG_CODE,Message.ADMIN_PASSWORD_WRONG));
            }
            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
