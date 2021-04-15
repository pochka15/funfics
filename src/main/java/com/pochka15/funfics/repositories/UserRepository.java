package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.JoinType;
import java.util.Optional;

import static com.pochka15.funfics.utils.db.JpaUtils.with;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByName(String name);

    static Specification<User> withActivity() {
        return with("activity", JoinType.LEFT);
    }

    static Specification<User> withRoles() {
        return with("roles", JoinType.LEFT);
    }

    static Specification<User> id(Long id) {
        return (root, cq, cb) -> cb.equal(root.get("id"), id);
    }

    static Specification<User> name(String name) {
        return (root, cq, cb) -> cb.equal(root.get("name"), name);
    }

}
