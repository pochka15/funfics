package com.pochka15.funfics.dto.form;

import com.pochka15.funfics.domain.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserRolesForm {
    private Long id;
    private Set<Role> roles;
}
