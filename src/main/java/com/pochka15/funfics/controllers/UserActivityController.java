package com.pochka15.funfics.controllers;

import com.pochka15.funfics.dto.AuthenticationResult;
import com.pochka15.funfics.dto.UserWithoutActivityDto;
import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.CredentialsForm;
import com.pochka15.funfics.dto.form.LoginForm;
import com.pochka15.funfics.exceptions.PasswordsNotMatched;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.services.AuthenticationService;
import com.pochka15.funfics.services.users.UserManagementService;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static com.pochka15.funfics.configs.SpringFoxConfig.API_KEY_NAME;

/**
 * Controller that is primarily made to map register and login requests
 */
@RestController
public class UserActivityController {
    private final UserManagementService userManagementService;
    private final AuthenticationService authenticationService;

    public UserActivityController(UserManagementService userManagementService,
                                  AuthenticationService authenticationService) {
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login")
    public AuthenticationResult login(@RequestBody LoginForm form) throws AuthenticationException {
        return authenticationService.passAuthentication(form);
    }

    @PostMapping(path = "/register")
    public UserWithoutActivityDto register(@RequestBody @Valid CredentialsForm credentials) {
        return userManagementService.saveNewUser(credentials);
    }

    @PostMapping("/change-password")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordForm form, Principal principal)
            throws UserNotFound, PasswordsNotMatched {
        userManagementService.changeUserPassword(principal.getName(), form);
        return ResponseEntity.ok().build();
    }
}
