package com.pochka15.funfics.converters.funfics;

import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficContent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FunficFormToFunficConverter implements Converter<SaveFunficForm, Funfic> {
    @Override
    public @NonNull
    Funfic convert(SaveFunficForm source) {
        return Funfic.builder()
                .description(source.getDescription())
                .funficContent(new FunficContent(source.getContent()))
                .name(source.getName())
                .tags(source.getTags())
                .genre(source.getGenre())
                .build();
    }
}
