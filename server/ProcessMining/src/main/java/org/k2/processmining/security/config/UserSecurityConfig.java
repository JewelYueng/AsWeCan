package org.k2.processmining.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by nyq on 2017/6/24.
 */
@Configuration
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private IUserDetailService iUserDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(iUserDetailService);
        auth.authenticationProvider(authenticationProvider);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/loginPage", "/js/**").permitAll()
                .antMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/home/login").loginProcessingUrl("/login").permitAll()
                .and()
                .csrf().disable()
                .httpBasic();
    }
}
