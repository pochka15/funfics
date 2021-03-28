package com.pochka15.funfics.dto;

import com.pochka15.funfics.entities.user.UserActivity;
import lombok.Data;

@Data
public class UserDto {
    private final boolean enabled;
    private final UserActivity activity;
    private String name;
    private Long id;
    private String email;

    public UserDto(String name, Long id, String email, boolean enabled, UserActivity activity) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.activity = activity;
    }
}
