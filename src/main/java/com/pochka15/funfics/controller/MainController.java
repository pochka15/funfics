package com.pochka15.funfics.controller;

import com.pochka15.funfics.config.converter.FunficToDtoConverter;
import com.pochka15.funfics.dto.FunficDto;
import com.pochka15.funfics.service.FunficService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class MainController {
    private final FunficService funficService;
    private final FunficToDtoConverter funficToDtoConverter;

    public MainController(FunficService funficService,
                          FunficToDtoConverter funficToDtoConverter) {
        this.funficService = funficService;
        this.funficToDtoConverter = funficToDtoConverter;
    }

    @GetMapping("/funfics")
    public List<FunficDto> home() {
        return StreamSupport.stream(funficService.fetchAllFunfics().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
