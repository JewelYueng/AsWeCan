package org.k2.processmining.security;

import org.k2.processmining.model.user.Administrator;
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

    public static String ADMIN_NOT_FOUND = "admin not found";

    @Autowired
    AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        System.out.println("AdminDetailService:"+s);

        Administrator administrator = adminService.getAdminByWorkId(s);
        if (administrator == null){
            throw new UsernameNotFoundException(ADMIN_NOT_FOUND);
        }
        AdminDetails adminDetails = new AdminDetails(administrator);
        adminDetails.addAuthority(AdminDetails.ROLE_ADMIN);
        return adminDetails;
    }
}
