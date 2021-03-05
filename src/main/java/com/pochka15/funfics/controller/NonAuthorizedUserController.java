package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.service.FunficsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    public NonAuthorizedUserController(
            FunficsService funficsService) {
        this.funficsService = funficsService;
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
}