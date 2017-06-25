package org.k2.processmining.security.config;

import org.k2.processmining.model.user.User;
import org.k2.processmining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by nyq on 2017/6/24.
 */
@Service("iUserDetailService")
public class IUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("email not found");
        }
        IUserDetail iUserDetail = new IUserDetail(user);
        iUserDetail.addAuthority(IUserDetail.ROLE_USER);
        return iUserDetail;
    }
}
