package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectFunficAuthor;
import com.pochka15.funfics.exceptions.UserNotFound;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FunficsService {
    /**
     * @return a list of funfic data structures that don't contain the content of funfics
     */
    List<FunficDto> fetchAllFunfics();

    FunficDto saveFunfic(SaveFunficForm form, String authorName) throws UserNotFound;

    Optional<FunficWithContentDto> fetchFunficById(long id);

    List<FunficDto> fetchFunficsByAuthor(String authorName);

    void deleteFunfics(String authorName, Collection<Long> funficIds) throws IncorrectFunficAuthor;

    /**
     * Update the funfic data (e.x. content, description...) from the form
     *
     * @param form   - contains new funfic data
     * @param author - the username
     * @return FunficDto - an updated funfic
     * @throws FunficDoesntExist     - when the funfic is not found in the database
     * @throws IncorrectFunficAuthor - when the given author is not the same as the author of the funfic
     */
    FunficDto updateFunfic(UpdateFunficForm form, String author) throws FunficDoesntExist, IncorrectFunficAuthor;
}
