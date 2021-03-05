package com.pochka15.funfics.dto.funfic;

import com.pochka15.funfics.domain.funfic.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Dto containing the main funfic info and it's content
 */
@Builder
@Data
public class FunficWithContentDto {
    private Long id;

    private Genre genre;

    @Builder.Default
    private Set<String> tags = Set.of();

    private String name;
    private String description;
    private float rating;
    private String content;
}

