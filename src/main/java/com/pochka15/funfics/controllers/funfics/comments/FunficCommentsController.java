package com.pochka15.funfics.controllers.funfics.comments;

import com.pochka15.funfics.dto.form.SaveCommentForm;
import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.services.funfics.CommentsService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/funfics/comments")
public class FunficCommentsController {
    private final CommentsService commentsService;

    public FunficCommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @MessageMapping("/comments/save")
    @SendTo("/sock/comments")
    public CommentDto saveComment(@RequestBody @Valid SaveCommentForm comment, Principal principal)
            throws FunficDoesntExist, IncorrectAuthor {
        return commentsService.save(comment, principal.getName());
    }

    @GetMapping("/{funficId}")
    public List<CommentDto> funficComments(@PathVariable Long funficId) {
        return commentsService.funficComments(funficId);
    }

}
