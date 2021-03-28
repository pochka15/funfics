package com.pochka15.funfics.controllers;

import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.services.funfics.CommentsService;
import com.pochka15.funfics.services.funfics.FunficsSearchService;
import com.pochka15.funfics.services.funfics.FunficsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Controller for the requests that don't require a user to be authenticated
 */
@RestController
public class NonAuthorizedUserController {
    private final FunficsService funficsService;
    private final FunficsSearchService funficsSearchService;
    private final CommentsService commentsService;

    public NonAuthorizedUserController(FunficsService funficsService,
                                       FunficsSearchService funficsSearchService,
                                       CommentsService commentsService) {
        this.funficsService = funficsService;
        this.funficsSearchService = funficsSearchService;
        this.commentsService = commentsService;
    }

    /**
     * @return a list of funfic data structures that don't contain the content of funfics
     */
    @GetMapping("/funfics")
    public List<FunficDto> funficsWithoutContent() {
        return funficsService.fetchAllFunfics();
    }

    @GetMapping("/funfic")
    public FunficWithContentDto getSingleFunfic(@RequestParam Long id) {
        final Optional<FunficWithContentDto> fetched = funficsService.fetchFunficById(id);
        if (fetched.isPresent()) return fetched.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funfic with with the id " + id + " is Not Found");
    }

    @GetMapping("/search")
    public List<FunficDto> search(@RequestParam("query") String query) {
        return funficsSearchService.searchByName(query);
    }

    @GetMapping("/comments")
    public List<CommentDto> funficComments(@RequestParam Long id) {
        return commentsService.funficComments(id);
    }

}