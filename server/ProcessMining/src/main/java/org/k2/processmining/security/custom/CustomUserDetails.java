package org.k2.processmining.security.custom;

import org.k2.processmining.model.user.User;
import org.k2.processmining.security.user.MyUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Aria on 2017/7/10.
 */
public class CustomUserDetails implements UserDetails {
    public static final String ROLE_USER = "ROLE_USER";
    private User user;
    private Collection<SimpleGrantedAuthority> authorities;

    public CustomUserDetails(User user){
        super();
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
        return user.getEmail();
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

    public void setPassword(String password){
        user.setPassword(password);
    }


    public void addAuthority(String Role){
        this.authorities.add(new SimpleGrantedAuthority(Role));
    }

    public User getUser() {
        return user;
    }

}
