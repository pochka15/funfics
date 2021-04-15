package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.CredentialsToUserConverter;
import com.pochka15.funfics.converters.users.UserToUserDtoConverter;
import com.pochka15.funfics.dto.UserWithoutActivityDto;
import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.CredentialsForm;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.exceptions.PasswordsNotMatched;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BaseUserManagementService implements UserManagementService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsToUserConverter credentialsToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public BaseUserManagementService(UserRepository userRepo,
                                     PasswordEncoder passwordEncoder,
                                     CredentialsToUserConverter credentialsToUserConverter,
                                     UserToUserDtoConverter userToUserDtoConverter) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.credentialsToUserConverter = credentialsToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }


    @Override
    public Optional<UserWithoutActivityDto> findByName(String name) {
        return userRepo.findByName(name).map(userToUserDtoConverter::convert);
    }

    @Override
    @Transactional
    public void changeUserPassword(String username, ChangePasswordForm form) throws UserNotFound, PasswordsNotMatched {
        User user = userRepo.findByName(username)
                .orElseThrow(() -> new UserNotFound("User is not found while changing the password"));
        if (passwordEncoder.matches(form.getCurrentPassword(), user.getPassword()))
            updatePassword(user, form.getNewPassword());
        else throw new PasswordsNotMatched("Given password doesn't match the existing one");
    }

    @Override
    @Transactional
    public void refreshLastLoginDate(String username) {
        userRepo.findByName(username).ifPresent(
                user -> user.getActivity().setLastLoginDate(LocalDateTime.now()));
    }

    private void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public UserWithoutActivityDto saveNewUser(CredentialsForm form) {
        User user = credentialsToUserConverter.convert(form);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userToUserDtoConverter.convert(userRepo.save(user));
    }
}