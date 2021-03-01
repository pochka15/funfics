package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.FunficToDtoConverter;
import com.pochka15.funfics.converter.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.dto.FunficDto;
import com.pochka15.funfics.dto.FunficFormDto;
import com.pochka15.funfics.dto.FunficWithContentDto;
import com.pochka15.funfics.repository.FunficRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FunficService {
    private final FunficRepository funficRepository;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;

    public FunficService(FunficRepository funficRepository,
                         FunficToDtoConverter funficToDtoConverter,
                         FunficFormToFunficConverter funficFormToFunficConverter,
                         FunficToFunficWithContentConverter funficToFunficWithContentConverter) {
        this.funficRepository = funficRepository;
        this.funficToDtoConverter = funficToDtoConverter;
        this.funficFormToFunficConverter = funficFormToFunficConverter;
        this.funficToFunficWithContentConverter = funficToFunficWithContentConverter;
    }

    public List<FunficDto> fetchAllFunfics() {
        return StreamSupport.stream(funficRepository.findAll().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Funfic saveFunfic(FunficFormDto form) {
        return funficRepository.save(funficFormToFunficConverter.convert(form));
    }

    public Optional<FunficWithContentDto> fetchFunficById(Long id) {
        return funficRepository.findById(id).map(funficToFunficWithContentConverter::convert);
    }
}
