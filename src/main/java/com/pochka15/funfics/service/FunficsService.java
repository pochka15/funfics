package com.pochka15.funfics.service;

import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficForm;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;

import java.util.List;
import java.util.Optional;

public interface FunficsService {
    List<FunficDto> fetchAllFunfics();

    Funfic saveFunfic(FunficForm form);

    Optional<FunficWithContentDto> fetchFunficById(Long id);
}
