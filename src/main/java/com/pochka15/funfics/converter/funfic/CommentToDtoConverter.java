package com.pochka15.funfics.converter.funfic;

import com.pochka15.funfics.domain.funfic.Comment;
import com.pochka15.funfics.dto.funfic.CommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentToDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(Comment comment) {
        return new CommentDto(comment.getId(),
                              comment.getContent(),
                              comment.getAuthor().getName());
    }
}
