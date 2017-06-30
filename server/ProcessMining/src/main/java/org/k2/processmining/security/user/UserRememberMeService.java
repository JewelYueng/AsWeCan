package org.k2.processmining.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aria on 2017/6/30.
 */
public class UserRememberMeService extends TokenBasedRememberMeServices{

    public static final String REMEMBER_ME_PARAMER = "userRemember";

    public UserRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("UserRememberService");

        UserDetails details = super.processAutoLoginCookie(cookieTokens, request, response);
        if (details == null) {
            System.out.println("details is null");
        }else {
            System.out.println("processAutoLoginCookie:name:"+details.getUsername()+"   pwd:"+details.getPassword());
        }
        return details;
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if ("application/json".equals(request.getHeader("Content-Type"))){
            String isOk = (String) request.getAttribute(REMEMBER_ME_PARAMER);
            System.out.println("userRemmber:"+isOk);
            if ("true".equalsIgnoreCase(isOk) || "yes".equalsIgnoreCase(isOk) || "on".equalsIgnoreCase(isOk) || "1".equalsIgnoreCase(isOk))
                return true;
            else return false;
        }else return super.rememberMeRequested(request,parameter);
    }
}
