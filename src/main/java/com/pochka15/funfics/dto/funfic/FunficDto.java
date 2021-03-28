package com.pochka15.funfics.dto.funfic;

import com.pochka15.funfics.entities.funfic.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class FunficDto {
    private Long id;

    private Genre genre;

    @Builder.Default
    private Set<String> tags = Set.of();

    private String name;
    private String description;
    private String author;
    private float rating;
}
