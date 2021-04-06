package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.entities.user.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class UserRoleToGrantedAuthorityConverter implements Converter<Role, GrantedAuthority> {
    @Override
    public GrantedAuthority convert(Role role) {
        return new SimpleGrantedAuthority(role.toString());
    }
}
