package org.k2.processmining.security.user;

import org.k2.processmining.security.admin.AdminDetailService;
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

    public static final String USER_NAME_NOT_FOUND = "user not found";
    public static final int USER_NAME_NOT_FOUND_CODE = 401;
    public static final String USER_PASSWORD_WRONG = "user password wrong";
    public static final int USER_PASSWORD_WRONG_CODE = 402;
    public static final String USER_VALIDATECODE_NULL = "user validateCode null";
    public static final int USER_VALIDATECODE_NULL_CODE = 403;
    public static final String USER_VALIDATECODE_WRONG = "user validateCode wrong";
    public static final int USER_VALIDATECODE_WRONG_CODE = 404;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        System.out.println("UserFailureHandler");
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            /*
             * USED if you want to AVOID redirect to LoginSuccessful.htm in JSON authentication
             */
            System.out.println(exception.getMessage());
            if (exception.getMessage().equals(USER_NAME_NOT_FOUND)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(USER_NAME_NOT_FOUND_CODE,USER_NAME_NOT_FOUND));
            }
            else if (exception.getMessage().equals(USER_VALIDATECODE_NULL)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(USER_VALIDATECODE_NULL_CODE,USER_VALIDATECODE_NULL));
            }else if (exception.getMessage().equals(USER_VALIDATECODE_WRONG)){
                response.getWriter().print(GsonParser.parseToCodeAndMessage(USER_VALIDATECODE_WRONG_CODE,USER_VALIDATECODE_WRONG));
            }
            else {
                System.out.println(exception.getMessage());
                response.getWriter().print(GsonParser.parseToCodeAndMessage(USER_PASSWORD_WRONG_CODE,USER_PASSWORD_WRONG));
            }
            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
