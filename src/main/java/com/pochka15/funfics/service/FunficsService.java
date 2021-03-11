package com.pochka15.funfics.service;

import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.NewFunficForm;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FunficsService {
    List<FunficDto> fetchAllFunfics();

    /**
     * @return true on success otherwise false
     */
    boolean saveFunfic(NewFunficForm form, String authorName);

    Optional<FunficWithContentDto> fetchFunficById(Long id);

    List<FunficDto> fetchFunficsByAuthor(String authorName);

    /**
     * @return true on success otherwise false
     */
    boolean deleteFunfics(String authorName, Collection<Long> funficIds);
}
