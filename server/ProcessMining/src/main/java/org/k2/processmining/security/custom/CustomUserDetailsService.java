package org.k2.processmining.security.custom;

import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.k2.processmining.security.user.MyUserDetails;
import org.k2.processmining.service.UserService;
import org.k2.processmining.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Aria on 2017/7/10.
 */
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.getUserByEmail(email);
        if (user == null){
            //查无此账号
            throw new UsernameNotFoundException(Message.USER_NAME_NOT_FOUND_CODE);
        }
        CustomUserDetails myUserDetails = new CustomUserDetails(user);
        myUserDetails.addAuthority(MyUserDetails.ROLE_USER);
        return myUserDetails;
    }
}
