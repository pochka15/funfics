package com.pochka15.funfics.dto.form.validator;

import com.pochka15.funfics.service.users.UserManagementService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom username validator that is used to create form constraints
 */
public class UserNameValidator implements ConstraintValidator<UsernameIsFree, String> {
    private final UserManagementService userManagementService;

    public UserNameValidator(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userManagementService.findByName(username).isEmpty();
    }
}
