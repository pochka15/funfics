package com.pochka15.funfics.service;

import com.pochka15.funfics.converter.funfic.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.converter.funfic.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.NewFunficForm;
import com.pochka15.funfics.repository.jpa.FunficsRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultFunficsService implements FunficsService {
    private final FunficsRepository funficsRepository;
    private final UserManagementService userManagementService;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;

    public DefaultFunficsService(FunficsRepository funficsRepository,
                                 UserManagementService userManagementService,
                                 FunficToDtoConverter funficToDtoConverter,
                                 FunficFormToFunficConverter funficFormToFunficConverter,
                                 FunficToFunficWithContentConverter funficToFunficWithContentConverter) {
        this.funficsRepository = funficsRepository;
        this.userManagementService = userManagementService;
        this.funficToDtoConverter = funficToDtoConverter;
        this.funficFormToFunficConverter = funficFormToFunficConverter;
        this.funficToFunficWithContentConverter = funficToFunficWithContentConverter;
    }

    @Override
    public List<FunficDto> fetchAllFunfics() {
        return StreamSupport.stream(funficsRepository.findAll().spliterator(), false)
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveFunfic(NewFunficForm form, String authorName) {
        final Funfic funfic = funficFormToFunficConverter.convert(form);
        final Optional<UserDto> found = userManagementService.findByName(authorName);
        if (found.isPresent()) {
            funfic.setAuthor(User.builder().id(found.get().getId()).build());
            funficsRepository.save(funfic);
            return true;
        }
        return false;
    }

    @Override
    public Optional<FunficWithContentDto> fetchFunficById(Long id) {
        return funficsRepository.findById(id).map(funficToFunficWithContentConverter::convert);
    }

    @Override
    public boolean deleteFunfics(String authorName, Collection<Long> funficIds) {
        if (checkIfIsAuthorOfGivenFunfics(authorName, funficIds)) {
            funficsRepository.deleteAll(
                    funficIds.stream()
                            .map(id -> Funfic.builder().id(id).build())
                            .collect(Collectors.toList()));
            return true;
        }
        return false;
    }

    private boolean checkIfIsAuthorOfGivenFunfics(String author, Collection<Long> ids) {
        Iterable<Funfic> foundFunfics = funficsRepository.findAllById(ids);
        return StreamSupport.stream(foundFunfics.spliterator(), true)
                .allMatch(funfic -> funfic.getAuthor().getName().equals(author));
    }

    @Override
    public List<FunficDto> fetchFunficsByAuthor(String authorName) {
        final Optional<UserDto> foundUserDto = userManagementService.findByName(authorName);
        return foundUserDto.map(
                userDto -> funficsRepository.findByAuthor(User.builder().id(userDto.getId()).build())
                        .stream()
                        .map(funficToDtoConverter::convert)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
}
