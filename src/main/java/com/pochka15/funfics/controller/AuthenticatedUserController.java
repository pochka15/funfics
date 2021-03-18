package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.DeleteFunficsForm;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.service.funfics.FunficsService;
import com.pochka15.funfics.service.users.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Controller for a user that has been authenticated
 */
@RestController
public class AuthenticatedUserController {
    private final FunficsService funficsService;
    private final UserManagementService userManagementService;

    public AuthenticatedUserController(FunficsService funficsService,
                                       UserManagementService userManagementService) {
        this.funficsService = funficsService;
        this.userManagementService = userManagementService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFunfic(@RequestBody SaveFunficForm form, Principal principal) {
        return funficsService.saveFunfic(form, principal.getName())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFunfic(@RequestBody UpdateFunficForm form, Principal principal)
            throws IncorrectAuthor, FunficDoesntExist {
        funficsService.updateFunfic(form, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordForm form, Principal principal) {
        return userManagementService.changeUserPassword(principal.getName(), form)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUserFunfics(@RequestBody DeleteFunficsForm form, Principal principal) {
        return funficsService.deleteFunfics(principal.getName(), form.getFunficIds())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/personal-funfics")
    public List<FunficDto> personalData(Principal principal) {
        return funficsService.fetchFunficsByAuthor(principal.getName());
    }
}
