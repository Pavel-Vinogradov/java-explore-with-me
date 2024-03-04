package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CreateOrUpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, CreateOrUpdateCommentDto commentDto);

    CommentDto patchByUser(Long userId, Long commentId, CreateOrUpdateCommentDto updateCommentDto);

    List<CommentDto> getUserComments(Long userId);

    List<CommentDto> getEventComments(Long eventId, Integer from, Integer size);

    void deleteComment(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> search(String text, Integer from, Integer size);
}