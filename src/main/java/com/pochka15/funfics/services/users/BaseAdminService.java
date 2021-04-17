package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.UserToUserForAdminTableDtoConverter;
import com.pochka15.funfics.dto.UserForAdmin;
import com.pochka15.funfics.entities.user.Role;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.entities.user.User_;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.UserRepository;
import com.pochka15.funfics.utils.db.JpaUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pochka15.funfics.utils.db.JpaUtils.with;

@Service
public class BaseAdminService implements AdminService {
    private final UserRepository userRepository;
    private final UserToUserForAdminTableDtoConverter userToUserForAdminTableDtoConverter;
    private final UtilityUserService utilityUserService;

    public BaseAdminService(UserRepository userRepository,
                            UserToUserForAdminTableDtoConverter userToUserForAdminTableDtoConverter,
                            UtilityUserService utilityUserService) {
        this.userRepository = userRepository;
        this.userToUserForAdminTableDtoConverter = userToUserForAdminTableDtoConverter;
        this.utilityUserService = utilityUserService;
    }

    @Override
    @Transactional
    public List<UserForAdmin> allUsers() {
        return userRepository
                .findAll(JpaUtils.<User>with(User_.ACTIVITY, JoinType.LEFT).and(with(User_.ROLES, JoinType.LEFT)))
                .stream()
                .map(userToUserForAdminTableDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserForAdmin fetchUserById(Long id) throws UserNotFound {
        final User user = userRepository
                .findOne(id(id).and(with(User_.ACTIVITY, JoinType.LEFT))
                                 .and(with(User_.ROLES, JoinType.LEFT)))
                .orElseThrow(() -> new UserNotFound("Couldn't find the user while fetching him for the admin"));
        return userToUserForAdminTableDtoConverter.convert(user);
    }

    private static Specification<User> id(Long id) {
        return (root, cq, cb) -> cb.equal(root.get(User_.ID), id);
    }

    @Override
    @Transactional
    public UserForAdmin blockUserById(Long id) throws UserNotFound {
        User user = utilityUserService.getUserOrThrow(
                id, "User with the id:  " + id + " wasn't found while trying to block him");
        user.setEnabled(false);
        return userToUserForAdminTableDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public UserForAdmin unblockUserById(Long id) throws UserNotFound {
        User user = utilityUserService.getUserOrThrow(id, "User with the id:  " + id + " wasn't found while unblocking him");
        user.setEnabled(true);
        return userToUserForAdminTableDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public UserForAdmin makeAdminById(Long id) throws UserNotFound {
        return setUserRoles(id, Collections.singleton(Role.ADMIN));
    }

    @Override
    @Transactional
    public UserForAdmin setUserRoles(Long id, Set<Role> roles) throws UserNotFound {
        User user = utilityUserService.getUserOrThrow(id, "User wasn't found while setting new roles for him");
        user.setRoles(roles);
        return userToUserForAdminTableDtoConverter.convert(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
