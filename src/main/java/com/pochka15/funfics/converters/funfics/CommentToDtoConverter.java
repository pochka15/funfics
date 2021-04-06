package com.pochka15.funfics.converters.funfics;

import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.entities.funfic.Comment;
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
