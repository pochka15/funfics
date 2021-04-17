package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.converters.funfics.FunficToDtoConverter;
import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithRatingDto;
import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficRating;
import com.pochka15.funfics.entities.funfic.Funfic_;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.exceptions.UserCannotRateFunfic;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.FunficRatingRepository;
import com.pochka15.funfics.repositories.FunficsRepository;
import com.pochka15.funfics.repositories.UserRepository;
import com.pochka15.funfics.utils.db.JpaUtils;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.pochka15.funfics.utils.db.JpaUtils.with;

@Service
public class BaseFunficRatingService implements FunficRatingService {
    private final FunficRatingRepository funficRatingRepository;
    private final UserRepository userRepository;
    private final FunficsRepository funficsRepository;
    private final FunficToDtoConverter funficToDtoConverter;

    public BaseFunficRatingService(FunficRatingRepository funficRatingRepository,
                                   UserRepository userRepository,
                                   FunficsRepository funficsRepository,
                                   FunficToDtoConverter funficToDtoConverter) {
        this.funficRatingRepository = funficRatingRepository;
        this.userRepository = userRepository;
        this.funficsRepository = funficsRepository;
        this.funficToDtoConverter = funficToDtoConverter;
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

    @Override
    public List<FunficWithRatingDto> fetchAllFunfics() {
        HashSet<Long> existingIds = new HashSet<>();
        return funficsRepository.findAll(
                JpaUtils.<Funfic>with(Funfic_.RATINGS, JoinType.LEFT)
                        .and(with(Funfic_.TAGS, JoinType.LEFT))
                        .and(with(Funfic_.AUTHOR, JoinType.LEFT)))
                .stream()
                .filter(funfic -> existingIds.add(funfic.getId()))
                .map(it -> withRating(funficToDtoConverter.convert(it), calculateAverageRating(it)))
                .collect(Collectors.toList());
    }

    private float calculateAverageRating(Funfic funfic) {
        AtomicInteger counter = new AtomicInteger();
        final float ratingSum = funfic.getRatings().stream()
                .map(FunficRating::getValue)
                .peek(v -> counter.getAndIncrement())
                .reduce(Float::sum).orElse(0f);
        return ratingSum == 0 ? 0 : ratingSum / counter.get();
    }

    private FunficWithRatingDto withRating(FunficDto dto, float rating) {
        return new FunficWithRatingDto(
                rating,
                dto.getId(),
                dto.getGenre(),
                dto.getTags(),
                dto.getName(),
                dto.getDescription(),
                dto.getAuthor());
    }
}
