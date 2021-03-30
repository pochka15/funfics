package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.CredentialsToUserConverter;
import com.pochka15.funfics.converters.users.UserToUserDtoConverter;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.CredentialsForm;
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
    public Optional<UserDto> findByName(String name) {
        return userRepo.findByName(name).map(userToUserDtoConverter::convert);
    }

    @Override
    @Transactional
    public boolean changeUserPassword(String username, ChangePasswordForm form) {
        var foundUser = userRepo.findByName(username);
        return foundUser.isPresent()
                && passwordEncoder.matches(form.getCurrentPassword(), foundUser.get().getPassword())
                && updatePassword(foundUser.get(), form.getNewPassword());
    }

    @Override
    @Transactional
    public void refreshLastLoginDate(String username) {
        userRepo.findByName(username).ifPresent(
                user -> user.getActivity().setLastLoginDate(LocalDateTime.now()));
    }

    private boolean updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public UserDto saveNewUser(CredentialsForm form) {
        User user = credentialsToUserConverter.convert(form);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userToUserDtoConverter.convert(userRepo.save(user));
    }
}