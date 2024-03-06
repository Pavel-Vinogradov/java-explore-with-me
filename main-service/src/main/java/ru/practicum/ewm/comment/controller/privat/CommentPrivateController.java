package ru.practicum.ewm.comment.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        newCommentDto.setEventId(eventId);
        return commentService.createComment(userId, newCommentDto);
    }

    @PatchMapping("/{commentId}/users/{userId}/")
    public CommentDto patchCommentByUser(@PathVariable Long userId, @PathVariable Long commentId,
                                         @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        updateCommentDto.setCommentId(commentId);
        return commentService.patchByUser(userId, updateCommentDto);
    }

    @GetMapping("/users/{userId}")
    public List<CommentDto> getUserComments(@PathVariable Long userId) {
        return commentService.getUserComments(userId);
    }

    @DeleteMapping("/{commentId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/users/{userId}/comments/{commentId}")
    public CommentDto getComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return commentService.getCommentUser(userId, commentId);
    }

}
