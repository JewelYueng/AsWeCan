package org.k2.processmining.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/6/21.
 */



public class UserLoginServiceImpl implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        System.out.println("用户名——————————————————————————"+userName);

        return null;
    }

}
