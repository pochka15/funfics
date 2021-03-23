package com.pochka15.funfics.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SaveCommentForm {
    private Long funficId;
    @NotEmpty(message = "Content must not be empty")
    private String content;
}
