package org.k2.processmining.security;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

/**
 * Created by Aria on 2017/7/8.
 */
public class CustomJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl{
    //重写remove的sql语句即可

    private static final String myRemoveSql = "delete from persistent_logins where username = ?";


    @Override
    public void removeUserTokens(String username) {
//        super.removeUserTokens(username);
    }
}
