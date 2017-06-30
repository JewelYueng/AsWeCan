package org.k2.processmining.security.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aria on 2017/6/30.
 */
public class AdminRememberService extends TokenBasedRememberMeServices {

    public AdminRememberService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }




}
