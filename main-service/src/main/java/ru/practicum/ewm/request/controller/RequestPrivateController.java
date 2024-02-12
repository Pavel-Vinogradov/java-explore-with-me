package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequests(@PathVariable @Positive long userId) {
        log.info("Запрос заявок юзера id = {}", userId);
        return requestService.getRequests(userId);
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> createRequest(@PathVariable @Positive long userId,
                                                @RequestParam @Positive long eventId) {
        log.info("Запрос на создание заявки от юзера id = {} на событие id = {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable @Positive long userId,
                                                @PathVariable @Positive long requestId) {
        log.info("Отмена заявки id = {} юзером id = {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsInEvent(@PathVariable @Positive long userId,
                                                     @PathVariable @Positive long eventId) {
        log.info("Запрос заявок на участие в событии id = {}", eventId);
        return requestService.getRequestsInEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> updateStatusRequests(
            @PathVariable @Positive long userId,
            @PathVariable @Positive long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Изменение статусов заявок на {}", eventRequestStatusUpdateRequest.getStatus());
        return requestService.updateStatusRequests(userId, eventId,
                eventRequestStatusUpdateRequest);
    }
}