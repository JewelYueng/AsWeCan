package org.k2.processmining.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Aria on 2017/6/23.
 */
public class MyAuthenticationProvider implements AuthenticationProvider{

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("authenticate");
        System.out.println(authentication.getDetails().toString());
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
