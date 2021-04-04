package com.pochka15.funfics.converters.users;

import com.pochka15.funfics.dto.UserForAdmin;
import com.pochka15.funfics.entities.user.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UserToUserForAdminTableDtoConverter implements Converter<User, UserForAdmin> {
    @Override
    public UserForAdmin convert(User source) {
        final LocalDateTime lastLoginDate = source.getActivity().getLastLoginDate();
        final String formattedLastLoginDate = lastLoginDate == null
                ? "" : lastLoginDate.format(DateTimeFormatter.ISO_DATE_TIME);
        return new UserForAdmin(source.getName(),
                                source.getId(),
                                source.isEnabled(),
                                source.getActivity().getRegistrationDate().format(DateTimeFormatter.ISO_DATE_TIME),
                                formattedLastLoginDate,
                                source.getRoles());
    }
}
