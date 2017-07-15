package org.k2.processmining.security;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Aria on 2017/7/5.
 */
public class CustomSaltSource implements SaltSource{
    @Override
    public Object getSalt(UserDetails userDetails) {
        return "666";
    }
}
