package com.pochka15.funfics.repository.jpa;

import com.pochka15.funfics.domain.funfic.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository extends CrudRepository<Comment, Long> {
}
