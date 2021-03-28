package com.pochka15.funfics.converters.funfics;

import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.dto.funfic.FunficDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FunficToDtoConverter implements Converter<Funfic, FunficDto> {
    @Override
    public FunficDto convert(Funfic source) {
        return FunficDto.builder()
                .name(source.getName())
                .rating(source.getRating())
                .description(source.getDescription())
                .tags(source.getTags())
                .id(source.getId())
                .genre(source.getGenre())
                .author(source.getAuthor().getName())
                .build();
    }
}
