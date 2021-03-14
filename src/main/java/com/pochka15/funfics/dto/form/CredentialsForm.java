package com.pochka15.funfics.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pochka15.funfics.dto.form.validator.UsernameIsFree;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CredentialsForm {
    @NotEmpty(message = "Name should not be empty")
    @Size(max = 20, message = "Name length should be <= 20")
    @UsernameIsFree
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(max = 64, min = 1, message = "Password length should be: 1 <= length <= 64")
    private String password;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be in correct form")
    private String email;

    @JsonProperty("isAdmin")
    private boolean isAdmin;
}