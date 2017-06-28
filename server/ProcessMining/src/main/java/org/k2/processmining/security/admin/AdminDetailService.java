package org.k2.processmining.security.admin;

import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.service.AdminService;
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
    public UserDetails loadUserByUsername(String workId) throws UsernameNotFoundException {

        System.out.println("AdminDetailService:"+ workId);

        Administrator administrator = adminService.getAdminByWorkId(workId);
        if (administrator == null){
            throw new UsernameNotFoundException(ADMIN_NOT_FOUND);
        }
        AdminDetails adminDetails = new AdminDetails(administrator);
        adminDetails.addAuthority(AdminDetails.ROLE_ADMIN);
        return adminDetails;
    }
}
