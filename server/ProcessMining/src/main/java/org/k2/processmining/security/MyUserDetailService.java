package org.k2.processmining.security;

import org.k2.processmining.model.user.User;
import org.k2.processmining.service.RawLogService;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aria on 2017/6/21.
 */


public class MyUserDetailService implements UserDetailsService{

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("userName:"+ email);
        //TODO
        //这里完成从数据库中查询对应的username
        User user = userService.getUserByEmail(email);
        if (user != null){
            System.out.println("user!= null");
            MyUserDetails myUserDetails = new MyUserDetails(user);
            myUserDetails.addAuthority(MyUserDetails.ROLE_USER);
            System.out.println("userPassword:"+myUserDetails.getPassword());
            return myUserDetails;
        }
        System.out.println("user == null");
        return null;
    }




}
