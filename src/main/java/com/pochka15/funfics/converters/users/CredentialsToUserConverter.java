package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.dto.form.CredentialsForm;
import com.pochka15.funfics.entities.user.Role;
import com.pochka15.funfics.entities.user.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CredentialsToUserConverter implements Converter<CredentialsForm, User> {
    @Override
    @NonNull
    public User convert(CredentialsForm source) {
        return User.builder()
                .name(source.getUsername())
                .password(source.getPassword())
                .email(source.getEmail())
                .roles(Collections.singleton(source.isAdmin() ? Role.ADMIN : Role.USER))
                .build();
    }
}