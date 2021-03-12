package com.pochka15.funfics.dto.form;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private String currentPassword;
    private String newPassword;
}
