package org.k2.processmining.security;

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

        System.out.println("AdminDetailService");



        return null;
    }
}
