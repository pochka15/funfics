package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.converter.funfic.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.converter.funfic.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.funfic.FunficContent;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.UserDto;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.repository.jpa.FunficsRepository;
import com.pochka15.funfics.service.users.UserManagementService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public boolean saveFunfic(SaveFunficForm form, String authorName) {
        final Optional<UserDto> found = userManagementService.findByName(authorName);
        if (found.isPresent()) {
            Funfic funfic = funficFormToFunficConverter.convert(form);
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

    @Override
    @Transactional
    public void updateFunfic(UpdateFunficForm form, String username) throws FunficDoesntExist, IncorrectAuthor {
        final Optional<Funfic> foundFunfic = funficsRepository.findById(form.getId());
        if (foundFunfic.isPresent()) {
            boolean sameAuthors = foundFunfic.get().getAuthor().getName().equals(username);
            if (sameAuthors) updateFunfic(foundFunfic.get(), form);
            else throw new IncorrectAuthor();
        } else throw new FunficDoesntExist();
    }

    private void updateFunfic(Funfic funfic, UpdateFunficForm form) {
        funfic.setFunficContent(new FunficContent(form.getContent()));
        funfic.setDescription(form.getDescription());
        funfic.setGenre(form.getGenre());
        funfic.setName(form.getName());
        funfic.setTags(form.getTags());
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
