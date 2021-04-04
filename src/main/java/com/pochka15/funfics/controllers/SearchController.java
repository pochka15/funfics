package com.pochka15.funfics.controllers;

import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.services.funfics.FunficsSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for the requests that don't require a user to be authenticated
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    private final FunficsSearchService funficsSearchService;

    public SearchController(FunficsSearchService funficsSearchService) {
        this.funficsSearchService = funficsSearchService;
    }

    @GetMapping("/funfics")
    public List<FunficDto> search(@RequestParam("query") String query) {
        return funficsSearchService.searchByName(query);
    }
}