package org.k2.processmining.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aria on 2017/6/21.
 */
public class UserAndPwdFilter extends UsernamePasswordAuthenticationFilter{

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "j_password";
    public static final String SPRING_SECURITY_FORM_REDERICT_KEY = "spring-security-redirect";

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private String redirectParameter = SPRING_SECURITY_FORM_REDERICT_KEY;
    private boolean postOnly = true;

    public  UserAndPwdFilter(){
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported: "+request.getMethod());
        }

        String userName = obtainUsername(request);
        String password = obtainPassword(request);

        return super.attemptAuthentication(request, response);
    }
}
