package com.pochka15.funfics.dto;

import lombok.Data;

@Data
public class UserWithoutActivityDto {
    private final boolean enabled;
    private String name;
    private Long id;
    private String email;

    public UserWithoutActivityDto(String name, Long id, String email, boolean enabled) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.enabled = enabled;
    }
}
