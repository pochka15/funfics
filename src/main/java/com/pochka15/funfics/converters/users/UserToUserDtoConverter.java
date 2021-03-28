package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {
        return new UserDto(user.getName(), user.getId(), user.getEmail(), user.isEnabled(), user.getActivity());
    }
}
