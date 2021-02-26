package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.FunficToDtoConverter;
import com.pochka15.funfics.dto.FunficDto;
import com.pochka15.funfics.repository.FunficRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FunficService {
    private final FunficRepository funficsRepository;
    private final FunficToDtoConverter funficToDtoConverter;

    public FunficService(FunficRepository funficsRepository,
                         FunficToDtoConverter funficToDtoConverter) {
        this.funficsRepository = funficsRepository;
        this.funficToDtoConverter = funficToDtoConverter;
    }

    public List<FunficDto> fetchAllFunfics() {
        return StreamSupport.stream(funficsRepository.findAll().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
