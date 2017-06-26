package org.k2.processmining.security;

import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Aria on 2017/6/21.
 */

public class MyUserDetails implements UserDetails{

    public static final String ROLE_USER = "ROLE_USER";
    private User user;
    private Collection<SimpleGrantedAuthority> authorities;

    public MyUserDetails(User user){
        super();
        this.user = user;
        authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getState() == UserState.ACTIVE.getValue() ||
                user.getState() == UserState.FREEZE.getValue();
    }

    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(String Role){
        this.authorities.add(new SimpleGrantedAuthority(Role));
    }

    public User getUser() {
        return user;
    }
}
