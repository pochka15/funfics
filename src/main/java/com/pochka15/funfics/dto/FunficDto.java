package com.pochka15.funfics.dto;

import com.pochka15.funfics.domain.funfic.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class FunficDto {
    private Genre genre;

    @Builder.Default
    private Set<String> tags = Set.of();

    private String name;
    private String description;
    private float rating;
}
