package com.pochka15.funfics.services.users;

import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilityUserService {
    private final UserRepository userRepository;

    public UtilityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserOrThrow(Long userId, String errorMessage) throws UserNotFound {
        User user;
        try {
            user = userRepository.getOne(userId);
        } catch (Exception e) {
            throw new UserNotFound(errorMessage);
        }
        return user;
    }

}
