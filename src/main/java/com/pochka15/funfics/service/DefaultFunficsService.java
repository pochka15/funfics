package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.funfic.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.converter.funfic.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficForm;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.repository.FunficRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultFunficsService implements FunficsService {
    private final FunficRepository funficRepository;
    private final UserManagementService userManagementService;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;

    public DefaultFunficsService(FunficRepository funficRepository,
                                 UserManagementService userManagementService,
                                 FunficToDtoConverter funficToDtoConverter,
                                 FunficFormToFunficConverter funficFormToFunficConverter,
                                 FunficToFunficWithContentConverter funficToFunficWithContentConverter) {
        this.funficRepository = funficRepository;
        this.userManagementService = userManagementService;
        this.funficToDtoConverter = funficToDtoConverter;
        this.funficFormToFunficConverter = funficFormToFunficConverter;
        this.funficToFunficWithContentConverter = funficToFunficWithContentConverter;
    }

    @Override
    public List<FunficDto> fetchAllFunfics() {
        return StreamSupport.stream(funficRepository.findAll().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveFunfic(FunficForm form, String author) {
        final Funfic funfic = funficFormToFunficConverter.convert(form);
        final Optional<UserDto> found = userManagementService.findByName(author);
        if (found.isPresent()) {
            funfic.setAuthor(User.builder().id(found.get().getId()).build());
            funficRepository.save(funfic);
        }
        return found.isPresent();
    }

    @Override
    public Optional<FunficWithContentDto> fetchFunficById(Long id) {
        return funficRepository.findById(id).map(funficToFunficWithContentConverter::convert);
    }
}
