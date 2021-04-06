package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.funfic.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository extends CrudRepository<Comment, Long> {
}
