package com.pochka15.funfics.services.users;

import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.CredentialsForm;

import java.util.Optional;

public interface UserManagementService {
    Optional<UserDto> findByName(String name);

    /**
     * @return saved user converted into the dto
     */
    UserDto saveNewUser(CredentialsForm form);

    /**
     * @return true on success otherwise false
     */
    boolean changeUserPassword(String username, ChangePasswordForm form);

    void refreshLastLoginDate(String username);
}
