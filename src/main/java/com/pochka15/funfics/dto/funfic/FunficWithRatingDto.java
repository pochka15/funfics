package com.pochka15.funfics.dto.funfic;

import com.pochka15.funfics.entities.funfic.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class FunficWithRatingDto {
    protected float rating;
    protected Long id;

    protected Genre genre;

    @Builder.Default
    protected Set<String> tags = Set.of();

    protected String name;
    protected String description;
    protected String author;
}
