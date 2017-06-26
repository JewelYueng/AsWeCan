package org.k2.processmining.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/26.
 */
public class UserLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    public static final String USER_NOT_FOUND = "101";
    public static final String USER_PWD_ERROR = "102";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (MyUserDetailService.USER_NOT_FOUND.equals(exception.getMessage())){
            System.out.println("登录失败！用户名不存在！");
            setDefaultFailureUrl("/home/login?error="+USER_NOT_FOUND);
        }else {
            System.out.println("登录失败！用户密码错误");
            setDefaultFailureUrl("/home/login?error="+USER_PWD_ERROR);
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
