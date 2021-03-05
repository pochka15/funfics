package com.pochka15.funfics.service;

import com.pochka15.funfics.dto.AuthenticationResult;
import com.pochka15.funfics.dto.form.LoginForm;
import com.pochka15.funfics.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public DefaultAuthenticationService(
            AuthenticationManager authenticationManager,
            DbUserDetailsService userDetailsService,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthenticationResult passAuthentication(LoginForm form) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(), form.getPassword()));
        return new AuthenticationResult(generateJwtToken(form.getUsername()));
    }

    private String generateJwtToken(String username) {
        return jwtUtils.generateToken(
                userDetailsService.loadUserByUsername(username));
    }
}
