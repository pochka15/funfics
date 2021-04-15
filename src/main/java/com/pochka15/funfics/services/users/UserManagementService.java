package com.pochka15.funfics.services.users;

import com.pochka15.funfics.dto.UserWithoutActivityDto;
import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.CredentialsForm;
import com.pochka15.funfics.exceptions.PasswordsNotMatched;
import com.pochka15.funfics.exceptions.UserNotFound;

import java.util.Optional;

public interface UserManagementService {
    Optional<UserWithoutActivityDto> findByName(String name);

    /**
     * @return saved user converted into the dto
     */
    UserWithoutActivityDto saveNewUser(CredentialsForm form);

    void changeUserPassword(String username, ChangePasswordForm form) throws UserNotFound, PasswordsNotMatched;

    void refreshLastLoginDate(String username);
}
