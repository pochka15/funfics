package com.pochka15.funfics.converter.funfic;

import com.pochka15.funfics.domain.funfic.Funfic;
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
                .build();
    }
}