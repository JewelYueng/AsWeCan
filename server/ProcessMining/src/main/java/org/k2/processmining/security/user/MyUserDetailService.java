package org.k2.processmining.security.user;

import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.k2.processmining.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/6/21.
 */


public class MyUserDetailService implements UserDetailsService{

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.getUserByEmail(email);
        if (user == null || user.getState() == UserState.DELETE.getValue()){
            //查无此账号
            throw new UsernameNotFoundException(Message.USER_NAME_NOT_FOUND_CODE);
        }
        if (user.getState() == UserState.FREEZE.getValue()){
            throw new UsernameNotFoundException(Message.USER_EMAIL_FREEZE_CODE);
        }
        if (user.getState() == UserState.FORBIDDEN.getValue()){
            throw new UsernameNotFoundException(Message.USER_FORBIDDEN_CODE);
        }
        System.out.println(user.getEmail()+"   "+user.getPassword());
            MyUserDetails myUserDetails = new MyUserDetails(user);
            myUserDetails.addAuthority(MyUserDetails.ROLE_USER);
            return myUserDetails;
    }




}
