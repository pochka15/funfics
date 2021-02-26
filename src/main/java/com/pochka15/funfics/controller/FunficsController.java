package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.FunficDto;
import com.pochka15.funfics.service.FunficService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FunficsController {
    private final FunficService funficService;

    public FunficsController(FunficService funficService) {
        this.funficService = funficService;
    }

    @GetMapping("/funfics")
    public List<FunficDto> home() {
        return funficService.fetchAllFunfics();
    }
}
