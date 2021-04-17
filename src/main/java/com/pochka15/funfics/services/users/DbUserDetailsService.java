package com.pochka15.funfics.services.users;

import com.pochka15.funfics.converters.users.UserToUserDetailsConverter;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.entities.user.User_;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;

import static com.pochka15.funfics.utils.db.JpaUtils.with;

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
        User user = userRepo
                .findOne(name(username).and(with(User_.ROLES, JoinType.LEFT)))
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find the user " + username));
        return userToUserDetailsConverter.convert(user);
    }

    private static Specification<User> name(String name) {
        return (root, cq, cb) -> cb.equal(root.get(User_.NAME), name);
    }
}