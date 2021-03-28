package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.UserToUserForAdminTableDtoConverter;
import com.pochka15.funfics.dto.UserForAdminTableDto;
import com.pochka15.funfics.entities.user.Role;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultAdminService implements AdminService {
    private final UserRepository userRepository;
    private final UserToUserForAdminTableDtoConverter userToUserForAdminTableDtoConverter;

    public DefaultAdminService(UserRepository userRepository,
                               UserToUserForAdminTableDtoConverter userToUserForAdminTableDtoConverter) {
        this.userRepository = userRepository;
        this.userToUserForAdminTableDtoConverter = userToUserForAdminTableDtoConverter;
    }

    @Override
    public List<UserForAdminTableDto> allUsers() {
        return userRepository.findAll().stream()
                .map(userToUserForAdminTableDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserForAdminTableDto> fetchUserById(Long id) {
        return userRepository.findById(id).map(userToUserForAdminTableDtoConverter::convert);
    }

    @Override
    @Transactional
    public boolean blockUserById(Long id) {
        Optional<User> found = userRepository.findById(id);
        found.ifPresent(user -> user.setEnabled(false));
        return found.isPresent();
    }

    @Override
    @Transactional
    public boolean makeAdminById(Long id) {
        return setUserRoles(id, Collections.singleton(Role.ADMIN));
    }

    @Override
    @Transactional
    public boolean setUserRoles(Long id, Set<Role> roles) {
        Optional<User> found = userRepository.findById(id);
        found.ifPresent(user -> user.setRoles(roles));
        return found.isPresent();
    }

    @Override
    public boolean deleteUserById(Long id) {
        userRepository.deleteById(id);
        return true;
    }
}
