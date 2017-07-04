package org.k2.processmining.security.user;

import org.k2.processmining.security.admin.AdminDetailService;
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
public class UserFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        System.out.println("UserFailureHandler");
        if ("application/json".equals(request.getHeader("Content-Type"))) {

            response.setHeader("Content-type", "application/json;charset=UTF-8");

            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            System.out.println(exception.getMessage());
            if (exception.getMessage().equals(Message.USER_NAME_NOT_FOUND_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_NAME_NOT_FOUND_CODE,Message.USER_NAME_NOT_FOUND));
            }
            else if (exception.getMessage().equals(Message.USER_VALIDATECODE_NULL_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_VALIDATECODE_NULL_CODE,Message.USER_VALIDATECODE_NULL));
            }
            else if (exception.getMessage().equals(Message.USER_VALIDATECODE_WRONG_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_VALIDATECODE_WRONG_CODE,Message.USER_VALIDATECODE_WRONG));
            }
            else if (exception.getMessage().equals(Message.USER_EMAIL_FREEZE_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_EMAIL_FREEZE_CODE,Message.USER_EMAIL_FREEZE));
            }
            else if (exception.getMessage().equals(Message.USER_FORBIDDEN_CODE)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_FORBIDDEN_CODE,Message.USER_FORBINDDEN));
            }
            else {
                System.out.println(exception.getMessage());
                response.getWriter().print(GsonParser.parseToCodeAndMessage(Message.USER_PASSWORD_WRONG_CODE,Message.USER_PASSWORD_WRONG));
            }
            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
