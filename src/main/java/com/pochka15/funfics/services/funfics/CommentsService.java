package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.dto.form.SaveCommentForm;
import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;

import java.util.List;

public interface CommentsService {
    CommentDto save(SaveCommentForm form, String author) throws FunficDoesntExist, IncorrectAuthor;

    List<CommentDto> funficComments(Long funficId);
}
