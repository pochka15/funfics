package com.pochka15.funfics.converter.funfic;

import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FunficToFunficWithContentConverter implements Converter<Funfic, FunficWithContentDto> {
    @Override
    public FunficWithContentDto convert(Funfic source) {
        return FunficWithContentDto.builder()
                .name(source.getName())
                .rating(source.getRating())
                .description(source.getDescription())
                .tags(source.getTags())
                .id(source.getId())
                .content(source.getFunficContent().getData())
                .genre(source.getGenre())
                .author(source.getAuthor().getName())
                .build();
    }
}
