package com.pochka15.funfics.services.users;

import com.pochka15.funfics.dto.UserForAdmin;
import com.pochka15.funfics.entities.user.Role;
import com.pochka15.funfics.exceptions.UserNotFound;

import java.util.List;
import java.util.Set;

public interface AdminService {
    List<UserForAdmin> allUsers();

    void deleteUserById(Long id);

    UserForAdmin fetchUserById(Long id) throws UserNotFound;

    UserForAdmin blockUserById(Long id) throws UserNotFound;

    UserForAdmin unblockUserById(Long id) throws UserNotFound;

    UserForAdmin makeAdminById(Long id) throws UserNotFound;

    UserForAdmin setUserRoles(Long id, Set<Role> roles) throws UserNotFound;
}
