package com.pochka15.funfics.converter.users;

import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.UserForAdminTableDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UserToUserForAdminTableDtoConverter implements Converter<User, UserForAdminTableDto> {
    @Override
    public UserForAdminTableDto convert(User source) {
        return new UserForAdminTableDto(source.getName(),
                                        source.getId(),
                                        source.isEnabled(),
                                        source.getActivity().getRegistrationDate().format(DateTimeFormatter.ISO_DATE),
                                        source.getRoles());
    }
}