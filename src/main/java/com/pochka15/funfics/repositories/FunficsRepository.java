package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FunficsRepository extends JpaRepository<Funfic, Long>, JpaSpecificationExecutor<Funfic> {
    List<Funfic> findByAuthor(User author);
}
