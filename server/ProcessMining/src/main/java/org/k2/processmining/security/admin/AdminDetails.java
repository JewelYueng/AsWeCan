package org.k2.processmining.security.admin;

import org.k2.processmining.model.user.Administrator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Aria on 2017/6/26.
 */
public class AdminDetails implements UserDetails{

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private Administrator admin;
    private Collection<SimpleGrantedAuthority> authorities;


    public AdminDetails(Administrator administrator){
        super();
        this.admin = administrator;
        authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getWorkId();
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

    public Administrator getAdmin() {
        return admin;
    }

    @Override
    public int hashCode() {
        return admin.getWorkId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("AdminDetails equals");
        if (obj instanceof AdminDetails){
            System.out.println("obj instanceof Administrator");
            return admin.getWorkId().equals(((AdminDetails)obj).getAdmin().getWorkId());
        }
        return super.equals(obj);
    }
}
