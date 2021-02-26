package com.pochka15.funfics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final DataSource datasource;

    public SecurityConfig(DataSource datasource,
                          PasswordEncoder passwordEncoder) {
        this.datasource = datasource;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
//                .antMatchers("/register").permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
//                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(datasource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select name, password, is_enabled from user where name=?")
                .authoritiesByUsernameQuery("select u.name, ur.roles from user u inner join user_role ur on u.id = ur.user_id where u.name=?");
    }

}
