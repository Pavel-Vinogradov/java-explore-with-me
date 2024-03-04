package ru.practicum.ewm.comment.mapper;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CreateOrUpdateCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, Long eventId) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .eventId(eventId)
                .created(comment.getCreated())
                .isEdited(comment.getIsEdited())
                .build();
    }

    public static Comment toComment(CreateOrUpdateCommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .isEdited(false)
                .build();
    }
}
