package com.pochka15.funfics.repositories;

import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficRating;
import com.pochka15.funfics.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FunficRatingRepository extends JpaRepository<FunficRating, Long> {
    Optional<FunficRating> findByUserAndFunfic(User user, Funfic funfic);

    @Query("SELECT AVG(r.value) FROM FunficRating r WHERE r.funfic = ?1")
    Optional<Float> averageFunficRating(Funfic funfic);
}
