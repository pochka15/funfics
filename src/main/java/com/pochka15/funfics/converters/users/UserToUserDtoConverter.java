package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.dto.UserWithoutActivityDto;
import com.pochka15.funfics.entities.user.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserWithoutActivityDto> {
    @Override
    public UserWithoutActivityDto convert(User user) {
        return new UserWithoutActivityDto(user.getName(), user.getId(), user.getEmail(), user.isEnabled());
    }
}
