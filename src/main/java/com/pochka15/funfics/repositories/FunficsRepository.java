package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.JoinType;
import java.util.List;

import static com.pochka15.funfics.utils.db.JpaUtils.with;

public interface FunficsRepository extends JpaRepository<Funfic, Long>, JpaSpecificationExecutor<Funfic> {
    List<Funfic> findByAuthor(User author);

    static Specification<Funfic> id(Long id) {
        return (root, cq, cb) -> cb.equal(root.get("id"), id);
    }

    static Specification<Funfic> withFunficContent() {
        return with("funficContent", JoinType.LEFT);
    }

    static Specification<Funfic> whereAuthorEquals(String authorName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("author").get("name"), authorName);
    }

    static Specification<Funfic> withAuthor() {
        return with("author", JoinType.LEFT);
    }

    static Specification<Funfic> withTags() {
        return with("tags", JoinType.LEFT);
    }

    static Specification<Funfic> withRatings() {
        return with("ratings", JoinType.LEFT);
    }
}
