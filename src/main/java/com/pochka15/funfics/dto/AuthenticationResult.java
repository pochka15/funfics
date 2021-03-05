package com.pochka15.funfics.dto;

import lombok.Data;

/**
 * The data structure that contains a jwt token.
 * It can be sent back to the client after the authentication process.
 */
@Data
public class AuthenticationResult {
    private final String jwt;

    public AuthenticationResult(String jwt) {
        this.jwt = jwt;
    }
}