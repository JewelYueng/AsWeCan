package org.k2.processmining.security.config;

import org.k2.processmining.mapper.AdminMapper;
import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by nyq on 2017/6/24.
 */
@Service("adminDetailService")
public class AdminDetailService implements UserDetailsService{

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrator administrator = adminMapper.getAdminByWorkId(username);
        if (administrator == null) {
            throw new UsernameNotFoundException("the wordid is not exist");
        }
        AdminDetail adminDetail = new AdminDetail(administrator);
        adminDetail.addAuthority(AdminDetail.ROLE_ADMIN);
        return adminDetail;
    }
}
