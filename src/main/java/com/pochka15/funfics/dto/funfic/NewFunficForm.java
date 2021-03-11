package com.pochka15.funfics.dto.funfic;

import com.pochka15.funfics.domain.funfic.Genre;
import lombok.Data;

import java.util.Set;


@Data
public class NewFunficForm {
    private Set<String> tags = Set.of();
    private String name;
    private String description;
    private Genre genre;
    private String content;
}
