package com.pochka15.funfics.controllers.funfics;

import com.pochka15.funfics.dto.form.DeleteFunficsForm;
import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.dto.funfic.*;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectFunficAuthor;
import com.pochka15.funfics.exceptions.UserCannotRateFunfic;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.services.funfics.FunficRatingService;
import com.pochka15.funfics.services.funfics.FunficsService;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.pochka15.funfics.configs.SpringFoxConfig.API_KEY_NAME;

/**
 * Controller for a user that has been authenticated
 */
@RestController
@RequestMapping("/funfics")
public class FunficsController {
    private final FunficsService funficsService;
    private final FunficRatingService funficRatingService;

    public FunficsController(FunficsService funficsService,
                             FunficRatingService funficRatingService) {
        this.funficsService = funficsService;
        this.funficRatingService = funficRatingService;
    }

    @PostMapping
    @Authorization(API_KEY_NAME)
    public FunficDto save(@RequestBody SaveFunficForm form, Principal principal) throws UserNotFound {
        return funficsService.saveFunfic(form, principal.getName());
    }

    @PostMapping("/update")
    @Authorization(API_KEY_NAME)
    public FunficDto update(@RequestBody UpdateFunficForm form, Principal principal)
            throws IncorrectFunficAuthor, FunficDoesntExist {
        return funficsService.updateFunfic(form, principal.getName());
    }

    @PostMapping("/delete-user-funfics")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> deleteUserFunfics(@RequestBody DeleteFunficsForm form, Principal principal) throws IncorrectFunficAuthor {
        funficsService.deleteFunfics(principal.getName(), form.getFunficIds());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-funfics")
    @Authorization(API_KEY_NAME)
    public List<FunficDto> userFunfics(Principal principal) {
        return funficsService.fetchFunficsByAuthor(principal.getName());
    }

    @GetMapping("/check-can-rate/{funficId}")
    @Authorization(API_KEY_NAME)
    public boolean checkUserCanRateFunfic(@PathVariable Long funficId, Principal principal) {
        return funficRatingService.checkIfUserCanRateFunfic(funficId, principal.getName());
    }

    @PostMapping("/rate")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> rate(@RequestBody RateFunficForm form, Principal principal) throws UserNotFound, UserCannotRateFunfic {
        funficRatingService.rateFunfic(form, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-without-content")
    public List<FunficWithRatingDto> funficsWithoutContent() {
        return funficRatingService.fetchAllFunfics();
    }

    @GetMapping("/{funficId}")
    public FunficWithContentDto singleFunfic(@PathVariable Long funficId) {
        final Optional<FunficWithContentDto> fetched = funficsService.fetchFunficById(funficId);
        if (fetched.isPresent()) return fetched.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funfic with with the id " + funficId + " is Not Found");
    }

    @GetMapping("/{funficId}/rating")
    public float funficRating(@PathVariable Long funficId) {
        return funficRatingService.averageRating(funficId);
    }
}
