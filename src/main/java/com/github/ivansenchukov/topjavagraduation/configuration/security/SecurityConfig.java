package com.github.ivansenchukov.topjavagraduation.configuration.security;

import com.github.ivansenchukov.topjavagraduation.model.Role;
import com.github.ivansenchukov.topjavagraduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    // todo - change it to non-deprecated
    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(encoder());

        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // todo - make this right, based on DB users table data
        // todo - translate this to test spring config after tests is done
//        // todo - change this hardcode to UserTestData properties after transfering this config to "test" directory
//        auth.inMemoryAuthentication().passwordEncoder(encoder()).withUser("firstuser@yandex.ru").password("password").roles(Role.ROLE_USER.getRoleName());
//        auth.inMemoryAuthentication().passwordEncoder(encoder()).withUser("seconduser@yandex.ru").password("password").roles(Role.ROLE_USER.getRoleName());
//        auth.inMemoryAuthentication().passwordEncoder(encoder()).withUser("admin@gmail.com").password("admin").roles(Role.ROLE_ADMIN.getRoleName());

        auth.authenticationProvider(authenticationProvider());

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
