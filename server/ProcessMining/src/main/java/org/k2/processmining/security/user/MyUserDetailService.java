package org.k2.processmining.security.user;

import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/6/21.
 */


public class MyUserDetailService implements UserDetailsService{

    public static final String USER_NOT_FOUND = "user not found";

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.getUserByEmail(email);

        if (user == null){
            //查无此账号
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
        System.out.println(user.getEmail()+"   "+user.getPassword());
            MyUserDetails myUserDetails = new MyUserDetails(user);
            myUserDetails.addAuthority(MyUserDetails.ROLE_USER);
        System.out.println("userName:"+myUserDetails.getUsername()+"  password:"+myUserDetails.getPassword());
            return myUserDetails;
    }




}
