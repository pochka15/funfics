package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.converters.funfics.FunficFormToFunficConverter;
import com.pochka15.funfics.converters.funfics.FunficToDtoConverter;
import com.pochka15.funfics.converters.funfics.FunficToFunficWithContentConverter;
import com.pochka15.funfics.dto.funfic.FunficDto;
import com.pochka15.funfics.dto.funfic.FunficWithContentDto;
import com.pochka15.funfics.dto.funfic.SaveFunficForm;
import com.pochka15.funfics.dto.funfic.UpdateFunficForm;
import com.pochka15.funfics.entities.funfic.Funfic;
import com.pochka15.funfics.entities.funfic.FunficContent;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectFunficAuthor;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.repositories.FunficsRepository;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BaseFunficsService implements FunficsService {
    private final FunficsRepository funficsRepository;
    private final FunficToDtoConverter funficToDtoConverter;
    private final FunficFormToFunficConverter funficFormToFunficConverter;
    private final FunficToFunficWithContentConverter funficToFunficWithContentConverter;
    private final UserRepository userRepository;

    public BaseFunficsService(FunficsRepository funficsRepository,
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
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FunficDto saveFunfic(SaveFunficForm form, String authorName) throws UserNotFound {
        final User user = userRepository.findByName(authorName)
                .orElseThrow(() -> new UserNotFound("User wasn't found while saving the funfic"));
        Funfic funfic = funficFormToFunficConverter.convert(form);
        funfic.setAuthor(user);
        return funficToDtoConverter.convert(funficsRepository.save(funfic));
    }

    @Override
    public Optional<FunficWithContentDto> fetchFunficById(long id) {
        return funficsRepository.findById(id).map(funficToFunficWithContentConverter::convert);
    }

    @Override
    public void deleteFunfics(String authorName, Collection<Long> funficIds) throws IncorrectFunficAuthor {
        if (checkIfIsAuthorOfGivenFunfics(authorName, funficIds)) {
            funficsRepository.deleteAll(
                    funficIds.stream()
                            .map(id -> Funfic.builder().id(id).build())
                            .collect(Collectors.toList()));
        } else throw new IncorrectFunficAuthor(
                "Couldn't delete funfics because " + authorName + " is not an author of them");
    }

    @Override
    @Transactional
    public FunficDto updateFunfic(UpdateFunficForm form, String author) throws FunficDoesntExist, IncorrectFunficAuthor {
        final Funfic funfic = funficsRepository.findById(form.getId())
                .orElseThrow(FunficDoesntExist::new);
        boolean sameAuthors = funfic.getAuthor().getName().equals(author);
        if (sameAuthors) {
            updateFunfic(funfic, form);
            return funficToDtoConverter.convert(funfic);
        } else throw new IncorrectFunficAuthor();
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
