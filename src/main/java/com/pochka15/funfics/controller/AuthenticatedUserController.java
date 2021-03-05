package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.funfic.FunficForm;
import com.pochka15.funfics.service.FunficsService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for a user that has been authenticated
 */
@RestController
public class AuthenticatedUserController {
    private final FunficsService funficsService;

    public AuthenticatedUserController(FunficsService funficsService) {
        this.funficsService = funficsService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFunfic(@RequestBody FunficForm form) {
        funficsService.saveFunfic(form);
        return ResponseEntity.ok().build();
    }
}
