package com.pochka15.funfics.dto.funfic;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String author;

    public CommentDto(Long id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }
}
