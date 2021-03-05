package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.CredentialsToUserConverter;
import com.pochka15.funfics.converter.UserToUserDtoConverter;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.form.CredentialsForm;
import com.pochka15.funfics.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserManagementService implements UserManagementService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsToUserConverter credentialsToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public DefaultUserManagementService(UserRepository userRepo,
                                        PasswordEncoder passwordEncoder,
                                        CredentialsToUserConverter credentialsToUserConverter,
                                        UserToUserDtoConverter userToUserDtoConverter) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.credentialsToUserConverter = credentialsToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }


    @Override
    public Optional<UserDto> findByName(String name) {
        return userRepo.findByName(name).map(userToUserDtoConverter::convert);
    }

    @Override
    public UserDto saveNewUser(CredentialsForm form) {
        User user = credentialsToUserConverter.convert(form);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userToUserDtoConverter.convert(userRepo.save(user));
    }
}