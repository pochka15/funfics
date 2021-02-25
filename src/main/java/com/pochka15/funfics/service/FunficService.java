package com.pochka15.funfics.service;

import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.repository.FunficRepository;
import org.springframework.stereotype.Service;

@Service
public class FunficService {
    private final FunficRepository funficsRepository;

    public FunficService(FunficRepository funficsRepository) {
        this.funficsRepository = funficsRepository;
    }

    public Iterable<Funfic> fetchAllFunfics() {
        return funficsRepository.findAll();
    }
}
