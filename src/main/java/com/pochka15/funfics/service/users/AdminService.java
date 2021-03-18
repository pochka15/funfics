package com.pochka15.funfics.service.users;

import com.pochka15.funfics.domain.user.Role;
import com.pochka15.funfics.dto.UserForAdminTableDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {
    List<UserForAdminTableDto> allUsers();

    boolean deleteUserById(Long id);

    Optional<UserForAdminTableDto> fetchUserById(Long id);

    boolean blockUserById(Long id);

    boolean makeAdminById(Long id);

    boolean setUserRoles(Long id, Set<Role> roles);
}