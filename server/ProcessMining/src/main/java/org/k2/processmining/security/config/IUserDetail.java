package org.k2.processmining.security.config;

import org.k2.processmining.model.UserState;
import org.k2.processmining.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nyq on 2017/6/24.
 */
public class IUserDetail implements UserDetails{

    private User user;
    public static final String ROLE_USER = "ROLE_USER";
    private Collection<SimpleGrantedAuthority> authorities;

    public IUserDetail(User user) {
        this.user = user;
        authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    public void setUser(User user) {
        this.user = user;
    }
}
