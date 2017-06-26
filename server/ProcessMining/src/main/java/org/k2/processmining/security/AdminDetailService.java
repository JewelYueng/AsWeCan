package org.k2.processmining.security;

import org.k2.processmining.model.user.User;
import org.k2.processmining.service.AdminService;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/6/23.
 */
public class AdminDetailService implements UserDetailsService{
//
    @Autowired
    AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        System.out.println("AdminDetailService:"+s);

        User user  = new User();
        user.setName(s);
        user.setPassword("123456");
        MyUserDetails myUserDetails = new MyUserDetails(user);
        myUserDetails.addAuthority("ROLE_ADMIN");
        myUserDetails.addAuthority("ROLE_USER");
        return myUserDetails;
    }
}
