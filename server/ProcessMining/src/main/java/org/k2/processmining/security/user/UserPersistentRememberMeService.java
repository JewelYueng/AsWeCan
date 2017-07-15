package org.k2.processmining.security.user;

import org.k2.processmining.util.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nyq on 2017/7/8.
 */
public class UserPersistentRememberMeService extends PersistentTokenBasedRememberMeServices{
    public static final String REMEMBER_ME_PARAMER = "userRemember";

    private boolean leftOne;

    public UserPersistentRememberMeService(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        leftOne = false;
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        logout(request,response,successfulAuthentication); //删除token
        super.onLoginSuccess(request, response, successfulAuthentication);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if ("application/json".equals(request.getHeader("Content-Type"))){
            String isOk = (String) request.getAttribute(REMEMBER_ME_PARAMER);
            System.out.println("userRemember:"+isOk);
            if ("true".equalsIgnoreCase(isOk) || "yes".equalsIgnoreCase(isOk) || "on".equalsIgnoreCase(isOk) || "1".equalsIgnoreCase(isOk))
                return true;
            else return false;
        }else return super.rememberMeRequested(request,parameter);
    }
}
