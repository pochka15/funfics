package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunficsRepository extends JpaRepository<Funfic, Long> {
    List<Funfic> findByAuthor(User author);
}
