package com.pochka15.funfics.controllers;

import com.pochka15.funfics.dto.form.ChangePasswordForm;
import com.pochka15.funfics.dto.form.DeleteFunficsForm;
import com.pochka15.funfics.dto.form.RateFunficForm;
import com.pochka15.funfics.dto.form.SaveCommentForm;
import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.services.funfics.CommentsService;
import com.pochka15.funfics.services.funfics.FunficRatingService;
import com.pochka15.funfics.services.funfics.FunficsService;
import com.pochka15.funfics.services.users.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Controller for a user that has been authenticated
 */
@RestController
public class AuthenticatedUserController {
    private final FunficsService funficsService;
    private final UserManagementService userManagementService;
    private final CommentsService commentsService;
    private final FunficRatingService funficRatingService;

    public AuthenticatedUserController(FunficsService funficsService,
                                       UserManagementService userManagementService,
                                       CommentsService commentsService,
                                       FunficRatingService funficRatingService) {
        this.funficsService = funficsService;
        this.userManagementService = userManagementService;
        this.commentsService = commentsService;
        this.funficRatingService = funficRatingService;
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

    @MessageMapping("/save-comment")
    @SendTo("/sock/comment-listeners")
    public CommentDto saveComment(@RequestBody @Valid SaveCommentForm comment, Principal principal)
            throws FunficDoesntExist, IncorrectAuthor {
        return commentsService.save(comment, principal.getName());
    }

    @GetMapping("/can-rate")
    public boolean checkUserCanRateFunfic(@RequestParam long funficId, Principal principal) {
        return funficRatingService.checkIfUserCanRateFunfic(funficId, principal.getName());
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateFunfic(@RequestBody RateFunficForm form, Principal principal) {
        return funficRatingService.rateFunfic(form, principal.getName())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
