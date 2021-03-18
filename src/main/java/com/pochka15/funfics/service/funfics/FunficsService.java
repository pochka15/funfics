package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FunficsService {
    List<FunficDto> fetchAllFunfics();

    /**
     * @return true on success otherwise false
     */
    boolean saveFunfic(SaveFunficForm form, String authorName);

    Optional<FunficWithContentDto> fetchFunficById(Long id);

    List<FunficDto> fetchFunficsByAuthor(String authorName);

    /**
     * @return true on success otherwise false
     */
    boolean deleteFunfics(String authorName, Collection<Long> funficIds);

    void updateFunfic(UpdateFunficForm form, String username) throws FunficDoesntExist, IncorrectAuthor;
}
