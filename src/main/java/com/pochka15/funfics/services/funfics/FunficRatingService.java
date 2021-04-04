package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.exceptions.UserCannotRateFunfic;
import com.pochka15.funfics.exceptions.UserNotFound;

public interface FunficRatingService {
    /**
     * @param funficId id if the funfic
     * @param username username which gave the
     * @return <b>true</b> if there is no rating given by the 'username' for the funfic with the id funficId
     */
    boolean checkIfUserCanRateFunfic(long funficId, String username);

    /**
     * @param rateFunficForm just input data
     * @param username       name of the user who rated the funfic
     */
    void rateFunfic(RateFunficForm rateFunficForm, String username) throws UserNotFound, UserCannotRateFunfic;

    float averageRating(long funficId);
}
