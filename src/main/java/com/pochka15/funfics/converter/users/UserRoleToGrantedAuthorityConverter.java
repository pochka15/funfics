package com.pochka15.funfics.converter.users;

import com.pochka15.funfics.domain.user.Role;
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
