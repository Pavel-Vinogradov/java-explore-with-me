package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, NewCommentDto commentDto);

    CommentDto patchByUser(Long userId, UpdateCommentDto updateCommentDto);

    List<CommentDto> getUserComments(Long userId);

    List<CommentDto> getEventComments(Long eventId, Integer from, Integer size);

    void deleteComment(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> search(String text, Integer from, Integer size);

    CommentDto getCommentUser(Long userId, Long commentId);

    CommentDto getComment(Long commentId);

}