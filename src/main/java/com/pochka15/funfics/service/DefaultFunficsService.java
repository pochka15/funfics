package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.funfic.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.converter.funfic.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficForm;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.repository.FunficRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultFunficsService implements FunficsService {
    private final FunficRepository funficRepository;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;

    public DefaultFunficsService(FunficRepository funficRepository,
                                 FunficToDtoConverter funficToDtoConverter,
                                 FunficFormToFunficConverter funficFormToFunficConverter,
                                 FunficToFunficWithContentConverter funficToFunficWithContentConverter) {
        this.funficRepository = funficRepository;
        this.funficToDtoConverter = funficToDtoConverter;
        this.funficFormToFunficConverter = funficFormToFunficConverter;
        this.funficToFunficWithContentConverter = funficToFunficWithContentConverter;
    }

    @Override
    public List<FunficDto> fetchAllFunfics() {
        return StreamSupport.stream(funficRepository.findAll().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Funfic saveFunfic(FunficForm form) {
        return funficRepository.save(funficFormToFunficConverter.convert(form));
    }

    @Override
    public Optional<FunficWithContentDto> fetchFunficById(Long id) {
        return funficRepository.findById(id).map(funficToFunficWithContentConverter::convert);
    }
}
