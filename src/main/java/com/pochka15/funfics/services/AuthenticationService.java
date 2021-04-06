package com.pochka15.funfics.services;

import com.pochka15.funfics.dto.AuthenticationResult;
import com.pochka15.funfics.dto.form.LoginForm;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {
    AuthenticationResult passAuthentication(LoginForm form) throws AuthenticationException;
}
