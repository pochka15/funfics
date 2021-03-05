package com.pochka15.funfics.service;

import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.form.CredentialsForm;

import java.util.Optional;

public interface UserManagementService {
    Optional<UserDto> findByName(String name);
    UserDto saveNewUser(CredentialsForm form);
}
