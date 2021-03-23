package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.converter.funfic.CommentToDtoConverter;
import com.pochka15.funfics.domain.funfic.Comment;
import com.pochka15.funfics.domain.user.User;
import com.pochka15.funfics.dto.form.SaveCommentForm;
import com.pochka15.funfics.dto.funfic.CommentDto;
import com.pochka15.funfics.exceptions.FunficDoesntExist;
import com.pochka15.funfics.exceptions.IncorrectAuthor;
import com.pochka15.funfics.repository.jpa.CommentsRepository;
import com.pochka15.funfics.repository.jpa.FunficsRepository;
import com.pochka15.funfics.repository.jpa.UserRepository;
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
            return commentToDtoConverter.convert(
                    commentsRepository.save(
                            buildComment(form, foundUser.get())));
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
