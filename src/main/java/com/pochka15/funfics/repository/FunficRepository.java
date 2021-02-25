package com.pochka15.funfics.repository;

import com.pochka15.funfics.domain.funfic.Funfic;
import org.springframework.data.repository.CrudRepository;

public interface FunficRepository extends CrudRepository<Funfic, Long> {
}
