package com.pochka15.funfics.configs;

import com.pochka15.funfics.configs.filters.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers(HttpMethod.GET,
                             "/search/funfics",
                             "/funfics/comments/*",
                             "/funfics/*",
                             "/funfics/*/rating",
                             "/funfics/all-without-content",
                             "/funfics/*",
                             "/comments-websocket/**",
//                             Swagger
                             "/configuration/ui",
                             "/configuration/security",
                             "/swagger-ui.html",
                             "/swagger-ui/**",
                             "/swagger-resources/**",
                             "/swagger-ui.html",
                             "/webjars/**",
                             "/v2/api-docs",
                             // -- Swagger UI v3 (OpenAPI)
                             "/v3/api-docs/**",
                             "/swagger-ui/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                .and().cors()
                .and().csrf().disable();
        security.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
