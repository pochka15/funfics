package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficRating;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.repositories.FunficRatingRepository;
import com.pochka15.funfics.repositories.FunficsRepository;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    //    TODO(@pochka15): test it, there will be a lot of selects/inserts
//    refactor this
    @Override
    @Transactional
    public boolean rateFunfic(RateFunficForm rateFunficForm, String username) {
        final long funficId = rateFunficForm.getFunficId();
        if (checkIfUserCanRateFunfic(funficId, username)) {
            final Optional<User> foundUser = userRepository.findByName(username);
            if (foundUser.isPresent()) {
                final Funfic funfic = funficsRepository.getOne(funficId);
                final FunficRating builtRating = FunficRating
                        .builder()
                        .funfic(funfic)
                        .value(rateFunficForm.getRating())
                        .user(foundUser.get())
                        .build();

                funficRatingRepository.save(builtRating);

                return true;
            }
        }
        return false;
    }

    @Override
    public float averageRating(long funficId) {
        return funficRatingRepository.averageFunficRating(funficsRepository.getOne(funficId));
    }
}
