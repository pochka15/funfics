package com.pochka15.funfics.utils.db;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;

public class JpaUtils {
    public static boolean isCountQuery(CriteriaQuery<?> criteriaQuery) {
        return Long.class == criteriaQuery.getResultType();
    }

    public static <T> Specification<T> with(String fieldName, JoinType joinType) {
        return (root, cq, cb) -> {
            if (!isCountQuery(cq)) root.fetch(fieldName, joinType);
            return null;
        };
    }
}
