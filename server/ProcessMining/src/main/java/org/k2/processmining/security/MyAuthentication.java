package org.k2.processmining.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * Created by Aria on 2017/6/26.
 */
public class MyAuthentication implements AuthenticationDetailsSource{
    @Override
    public Object buildDetails(Object o) {
        return null;
    }
}
