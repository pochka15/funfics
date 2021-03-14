package com.pochka15.funfics.dto;

import com.pochka15.funfics.domain.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserForAdminTableDto {
    private boolean enabled;
    private String name;
    private Long id;
    private String registrationDate;
    private Set<Role> roles;

    public UserForAdminTableDto(String name, Long id, boolean enabled, String registrationDate, Set<Role> roles) {
        this.name = name;
        this.id = id;
        this.registrationDate = registrationDate;
        this.enabled = enabled;
        this.roles = roles;
    }
}
