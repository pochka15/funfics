package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.FunficDto;
import com.pochka15.funfics.dto.FunficFormDto;
import com.pochka15.funfics.dto.FunficWithContentDto;
import com.pochka15.funfics.service.FunficService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class FunficsController {
    private final FunficService funficService;

    public FunficsController(FunficService funficService) {
        this.funficService = funficService;
    }

    @GetMapping("/funfics")
    public List<FunficDto> funficsWithoutContent() {
        return funficService.fetchAllFunfics();
    }

    @GetMapping("/funfic")
    public FunficWithContentDto singleFunfic(@RequestParam Long id) {
        final Optional<FunficWithContentDto> fetched = funficService.fetchFunficById(id);
        if (fetched.isPresent()) return fetched.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funfic with with the id " + id + " is Not Found");
    }

    @PostMapping("/funfics")
    public String storeFunfic(@RequestBody FunficFormDto form) {
        funficService.saveFunfic(form);
        return form.toString();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handle(HttpMessageNotReadableException formatException) {
        return ResponseEntity.badRequest().body("Couldn't deserialize json" + formatException.getMessage());
    }
}