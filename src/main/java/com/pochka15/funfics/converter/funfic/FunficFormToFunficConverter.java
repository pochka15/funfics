package com.pochka15.funfics.converter.funfic;

import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.funfic.FunficContent;
import com.pochka15.funfics.dto.funfic.NewFunficForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FunficFormToFunficConverter implements Converter<NewFunficForm, Funfic> {
    @Override
    public @NonNull
    Funfic convert(NewFunficForm source) {
        return Funfic.builder()
                .description(source.getDescription())
                .funficContent(new FunficContent(source.getContent()))
                .name(source.getName())
                .tags(source.getTags())
                .genre(source.getGenre())
                .build();
    }
}
