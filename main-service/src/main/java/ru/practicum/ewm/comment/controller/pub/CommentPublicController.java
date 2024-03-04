package ru.practicum.ewm.comment.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CreateOrUpdateCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @Valid @RequestBody CreateOrUpdateCommentDto newCommentDto) {
        return commentService.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}/users/{userId}/")
    public CommentDto patchCommentByUser(@PathVariable Long userId, @PathVariable Long commentId,
                                         @Valid @RequestBody CreateOrUpdateCommentDto updateCommentDto) {

        return commentService.patchByUser(userId, commentId, updateCommentDto);
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

    @GetMapping("/{eventId}")
    public List<CommentDto> getCommentsAllCommentsByEvent(@PathVariable Long eventId,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getEventComments(eventId, from, size);
    }

}
