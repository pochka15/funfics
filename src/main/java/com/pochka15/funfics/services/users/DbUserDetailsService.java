package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.UserToUserDetailsConverter;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Service for loading <i>UserDetails</i> using the <i>UserRepository</i>
 */
@Service
public class DbUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    private final UserToUserDetailsConverter userToUserDetailsConverter;

    public DbUserDetailsService(UserRepository userRepo,
                                UserToUserDetailsConverter converter) {
        this.userRepo = userRepo;
        this.userToUserDetailsConverter = converter;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //                TODO(@pochka15): resolve n+1
        // 2 selects: user and user role
        User user = userRepo
                .findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find the user " + username));
        return userToUserDetailsConverter.convert(user);
    }
}