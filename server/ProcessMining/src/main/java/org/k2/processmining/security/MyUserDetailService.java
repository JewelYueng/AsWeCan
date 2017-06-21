package org.k2.processmining.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aria on 2017/6/21.
 */
public class MyUserDetailService implements UserDetailsService{

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("userName:"+username);
        //TODO
        //这里完成从数据库中查询对应的username
        MyUserDetails myUserDetails = new MyUserDetails("001",username,"123456",true);

        return myUserDetails;
    }


}
