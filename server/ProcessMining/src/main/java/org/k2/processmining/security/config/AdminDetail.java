package org.k2.processmining.security.config;

import org.k2.processmining.model.user.Administrator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nyq on 2017/6/24.
 */
public class AdminDetail implements UserDetails {
    private Administrator administrator;
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private Collection<SimpleGrantedAuthority> authorities;

    public AdminDetail(Administrator administrator) {
        this.administrator = administrator;
        authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return administrator.getPassword();
    }

    @Override
    public String getUsername() {
        return administrator.getWorkId();
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
        return true;
    }

    public void addAuthority(String Role){
        this.authorities.add(new SimpleGrantedAuthority(Role));
    }

    public Administrator getAdministrator() {
        return administrator;
    }
}
