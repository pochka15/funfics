package com.pochka15.funfics.dto;

import com.pochka15.funfics.domain.funfic.Genre;
import lombok.Data;

import java.util.Set;


@Data
public class FunficFormDto {
    private Set<String> tags = Set.of();
    private String name;
    private String description;
    private Genre genre;
    private String content;
}
