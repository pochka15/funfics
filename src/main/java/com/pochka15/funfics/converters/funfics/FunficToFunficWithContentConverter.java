package com.pochka15.funfics.converters.funfics;

import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.entities.funfic.Funfic;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FunficToFunficWithContentConverter implements Converter<Funfic, FunficWithContentDto> {
    @Override
    public FunficWithContentDto convert(Funfic source) {
        return FunficWithContentDto.builder()
                .name(source.getName())
                .description(source.getDescription())
                .tags(source.getTags())
                .id(source.getId())
                .content(source.getFunficContent().getData())
                .genre(source.getGenre())
                .author(source.getAuthor().getName())
                .build();
    }
}
