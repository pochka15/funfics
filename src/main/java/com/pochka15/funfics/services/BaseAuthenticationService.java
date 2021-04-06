package com.pochka15.funfics.services;

import com.pochka15.funfics.dto.AuthenticationResult;
import com.pochka15.funfics.dto.form.LoginForm;
import com.pochka15.funfics.services.users.DbUserDetailsService;
import com.pochka15.funfics.services.users.UserManagementService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthenticationService implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserManagementService userManagementService;
    private final JwtService defaultJwtService;

    public BaseAuthenticationService(
            AuthenticationManager authenticationManager,
            DbUserDetailsService userDetailsService,
            UserManagementService userManagementService, BaseJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userManagementService = userManagementService;
        this.defaultJwtService = jwtService;
    }

    @Override
    public AuthenticationResult passAuthentication(LoginForm form) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(), form.getPassword()));
        userManagementService.refreshLastLoginDate(form.getUsername());
        return new AuthenticationResult(generateJwtToken(form.getUsername()));
    }

    private String generateJwtToken(String username) {
        return defaultJwtService.generateToken(
                userDetailsService.loadUserByUsername(username));
    }
}
