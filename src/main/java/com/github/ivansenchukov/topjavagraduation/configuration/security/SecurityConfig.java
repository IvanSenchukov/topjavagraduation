package com.github.ivansenchukov.topjavagraduation.configuration.security;

import com.github.ivansenchukov.topjavagraduation.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // todo - change it to undeprecated
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        // todo - make this right, based on DB users table data
        // todo - translate this to test spring config after tests is done
        auth.inMemoryAuthentication().passwordEncoder(encoder).withUser("First_User").password("password").roles(Role.ROLE_USER.getRoleName());
        auth.inMemoryAuthentication().passwordEncoder(encoder).withUser("Second_User").password("password").roles(Role.ROLE_USER.getRoleName());
        auth.inMemoryAuthentication().passwordEncoder(encoder).withUser("Admin").password("admin").roles(Role.ROLE_ADMIN.getRoleName());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/rest/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/**").access("isAuthenticated()")
                .and().formLogin().defaultSuccessUrl("/", false);
        http.csrf().disable();
    }
}
