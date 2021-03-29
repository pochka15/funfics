package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.form.RateFunficForm;
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

    Optional<FunficWithContentDto> fetchFunficById(long id);

    List<FunficDto> fetchFunficsByAuthor(String authorName);

    /**
     * @return true on success otherwise false
     */
    boolean deleteFunfics(String authorName, Collection<Long> funficIds);

    /**
     * Update the funfic data (e.x. content, description...) from the form
     *
     * @param form   - contains new funfic data
     * @param author - the username
     * @throws FunficDoesntExist - when the funfic is not found in the database
     * @throws IncorrectAuthor   - when the given author is not the same as the author of the funfic
     */
    void updateFunfic(UpdateFunficForm form, String author) throws FunficDoesntExist, IncorrectAuthor;
}
