package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficRating;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.exceptions.UserCannotRateFunfic;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.FunficRatingRepository;
import com.pochka15.funfics.repositories.FunficsRepository;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BaseFunficRatingService implements FunficRatingService {
    private final FunficRatingRepository funficRatingRepository;
    private final UserRepository userRepository;
    private final FunficsRepository funficsRepository;

    public BaseFunficRatingService(FunficRatingRepository funficRatingRepository,
                                   UserRepository userRepository,
                                   FunficsRepository funficsRepository) {
        this.funficRatingRepository = funficRatingRepository;
        this.userRepository = userRepository;
        this.funficsRepository = funficsRepository;
    }

    @Override
    public boolean checkIfUserCanRateFunfic(long funficId, String username) {
        final Funfic funfic = funficsRepository.getOne(funficId);
        return userRepository
                .findByName(username)
                .filter(user -> !ratingExists(user, funfic))
                .isPresent();
    }

    private boolean ratingExists(User user, Funfic funfic) {
        return funficRatingRepository
                .findByUserAndFunfic(user, funfic)
                .isPresent();
    }

    @Override
    @Transactional
    public void rateFunfic(RateFunficForm rateFunficForm, String username) throws UserNotFound, UserCannotRateFunfic {
        final long funficId = rateFunficForm.getFunficId();
        if (checkIfUserCanRateFunfic(funficId, username)) {
            final User foundUser = userRepository
                    .findByName(username)
                    .orElseThrow(() -> new UserNotFound("Couldn't find the user while checking if he can rate a funfic"));
            saveRating(funficId, rateFunficForm.getRating(), foundUser);
        } else throw new UserCannotRateFunfic();
    }

    private void saveRating(Long funficId, float ratingValue, User userThatGaveRating) {
        final Funfic funfic = funficsRepository.getOne(funficId);
        funficRatingRepository.save(
                builtRating(funfic, ratingValue, userThatGaveRating));
    }

    private FunficRating builtRating(Funfic funfic, float ratingValue, User user) {
        return FunficRating
                .builder()
                .funfic(funfic)
                .value(ratingValue)
                .user(user)
                .build();
    }

    @Override
    public float averageRating(long funficId) {
        return funficRatingRepository
                .averageFunficRating(funficsRepository.getOne(funficId))
                .orElseGet(() -> (float) 0);
    }
}
