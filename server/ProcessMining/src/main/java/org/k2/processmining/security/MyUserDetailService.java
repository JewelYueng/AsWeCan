package org.k2.processmining.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/6/21.
 */
public class MyUserDetailService implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("userName:"+username);

        MyUserDetrails myUserDetrails = new MyUserDetrails("001",username,"123456",true);

        return myUserDetrails;
    }
}
