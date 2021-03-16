package com.pochka15.funfics.dto.funfic;

import com.pochka15.funfics.domain.funfic.Genre;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateFunficForm {
    private Long id;
    private Set<String> tags = Set.of();
    private String name;
    private String description;
    private Genre genre;
    private String content;
}
