package org.k2.processmining.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aria on 2017/6/26.
 */
public class AdminLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    public static final String ADMIN_NOT_FOUND = "101";
    public static final String ADMIN_PWD_ERROR = "102";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (AdminDetailService.ADMIN_NOT_FOUND.equals(exception.getMessage())){
            //账号不存在
            System.out.println("管理员账号不存在！");
            setDefaultFailureUrl("/admin/login?error="+ADMIN_NOT_FOUND);
        }else {
            //密码错误
            System.out.println("管理员密码错误！");
            setDefaultFailureUrl("/admin/login?error="+ADMIN_PWD_ERROR);
        }

        super.onAuthenticationFailure(request, response, exception);
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }
}
