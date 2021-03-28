package com.pochka15.funfics.repository.jpa;

import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunficsRepository extends JpaRepository<Funfic, Long> {
    List<Funfic> findByAuthor(User author);
}
