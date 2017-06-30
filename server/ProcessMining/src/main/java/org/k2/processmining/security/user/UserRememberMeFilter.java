package org.k2.processmining.security.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

/**
 * Created by Aria on 2017/6/30.
 */
public class UserRememberMeFilter extends RememberMeAuthenticationFilter{
    public UserRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }
}
