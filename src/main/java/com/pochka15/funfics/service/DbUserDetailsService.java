package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.UserToUserDetailsConverter;
import com.pochka15.funfics.repository.jpa.UserRepository;
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
        var found = userRepo.findByName(username);
        if (found.isPresent())
            return userToUserDetailsConverter.convert(found.get());
        else throw new UsernameNotFoundException("Couldn't find the user: " + username);
    }
}