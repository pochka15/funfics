package com.pochka15.funfics.dto;

import com.pochka15.funfics.domain.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserForAdminTableDto {
    private boolean enabled;
    private String name;
    private Long id;
    private String registrationIsoDateTime;
    private String lastLoginIsoDateTime;
    private Set<Role> roles;

    public UserForAdminTableDto(String name,
                                Long id,
                                boolean enabled,
                                String registrationIsoDateTime,
                                String lastLoginIsoDateTime,
                                Set<Role> roles) {
        this.name = name;
        this.id = id;
        this.registrationIsoDateTime = registrationIsoDateTime;
        this.enabled = enabled;
        this.lastLoginIsoDateTime = lastLoginIsoDateTime;
        this.roles = roles;
    }
}
