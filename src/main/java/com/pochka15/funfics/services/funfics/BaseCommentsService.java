package com.pochka15.funfics.services.funfics;

import com.pochka15.funfics.converters.funfics.CommentToDtoConverter;
import com.pochka15.funfics.entities.funfic.Comment;
import com.pochka15.funfics.entities.user.User;
import com.pochka15.funfics.dto.form.SaveCommentForm;
import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.repositories.CommentsRepository;
import com.pochka15.funfics.repositories.FunficsRepository;
import com.pochka15.funfics.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BaseCommentsService implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final CommentToDtoConverter commentToDtoConverter;
    private final FunficsRepository funficsRepository;
    private final UserRepository userRepository;

    public BaseCommentsService(CommentsRepository commentsRepository,
                               CommentToDtoConverter commentToDtoConverter,
                               FunficsRepository funficsRepository, UserRepository userRepository) {
        this.commentsRepository = commentsRepository;
        this.funficsRepository = funficsRepository;
        this.commentToDtoConverter = commentToDtoConverter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CommentDto save(SaveCommentForm form, String author)
            throws FunficDoesntExist, IncorrectAuthor {
        final Optional<User> foundUser = userRepository.findByName(author);
        if (foundUser.isPresent()) {
            Comment builtComment = buildComment(form, foundUser.get());
            Comment savedComment = commentsRepository.save(builtComment);
            return commentToDtoConverter.convert(savedComment);
        } else {
            throw new IncorrectAuthor("Comment author: " + author + " is not found");
        }
    }

    private Comment buildComment(SaveCommentForm form, User author) throws FunficDoesntExist {
        Comment.CommentBuilder builder = Comment.builder()
                .content(form.getContent())
                .author(author);
        try {
            builder.funfic(funficsRepository.getOne(form.getFunficId()));
        } catch (EntityNotFoundException e) {
            throw new FunficDoesntExist();
        }
        return builder.build();
    }

    @Override
    @Transactional
    public List<CommentDto> funficComments(Long funficId) {
        return funficsRepository.findById(funficId)
                .map(funfic -> toDto(funfic.getComments()))
                .orElseGet(List::of);
    }

    private List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream()
                .map(commentToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
