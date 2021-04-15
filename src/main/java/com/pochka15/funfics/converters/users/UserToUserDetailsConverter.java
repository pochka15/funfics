package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.entities.user.Role;
import com.pochka15.funfics.entities.user.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class UserToUserDetailsConverter implements Converter<User, UserDetails> {
    private final UserRoleToGrantedAuthorityConverter authorityConverter;

    public UserToUserDetailsConverter(UserRoleToGrantedAuthorityConverter authorityConverter) {
        this.authorityConverter = authorityConverter;
    }

    @Override
    public UserDetails convert(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                mapToAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> mapToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(authorityConverter::convert)
                .collect(Collectors.toList());
    }
}
