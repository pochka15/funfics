package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.converter.funfic.FunficFormToFunficConverter;
import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.converter.funfic.FunficToFunficWithContentConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.domain.funfic.FunficContent;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.repository.jpa.FunficsRepository;
import com.pochka15.funfics.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultFunficsService implements FunficsService {
    private final FunficsRepository funficsRepository;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;
    private final UserRepository userRepository;

    public DefaultFunficsService(FunficsRepository funficsRepository,
                                 FunficToDtoConverter funficToDtoConverter,
                                 FunficFormToFunficConverter funficFormToFunficConverter,
                                 FunficToFunficWithContentConverter funficToFunficWithContentConverter,
                                 UserRepository userRepository) {
        this.funficsRepository = funficsRepository;
        this.funficToDtoConverter = funficToDtoConverter;
        this.funficFormToFunficConverter = funficFormToFunficConverter;
        this.funficToFunficWithContentConverter = funficToFunficWithContentConverter;
        this.userRepository = userRepository;
    }

    @Override
    public List<FunficDto> fetchAllFunfics() {
        return funficsRepository.findAll().stream()
//                TODO(@pochka15): fix the n+1
                /*
                select from funfic
                select from user (because I say funfic.getUser().getName()

                Is it possible to use smth. like findAllWithUsers
                 */
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    //    TODO(@pochka15): I make select user then insert funfic: do one insert
    @Override
    @Transactional
    public boolean saveFunfic(SaveFunficForm form, String authorName) {
        final Optional<User> found = userRepository.findByName(authorName);
        if (found.isPresent()) {
            Funfic funfic = funficFormToFunficConverter.convert(form);
            funfic.setAuthor(found.get());
            funficsRepository.save(funfic);
            return true;
        }
        return false;
    }

    @Override
    public Optional<FunficWithContentDto> fetchFunficById(Long id) {
//                TODO(@pochka15): resolve n+1
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
        return funficsRepository.findAllById(ids).stream()
                .allMatch(funfic -> funfic.getAuthor().getName().equals(author));
    }

    //    TODO(@pochka15): fetch with funfics
    @Override
    public List<FunficDto> fetchFunficsByAuthor(String authorName) {
        final Optional<User> foundUser = userRepository.findByName(authorName);
        return foundUser
                .map(this::findUserFunfics)
                .orElseGet(List::of);
    }

    private List<FunficDto> findUserFunfics(User user) {
        return funficsRepository.findByAuthor(user)
                .stream()
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
