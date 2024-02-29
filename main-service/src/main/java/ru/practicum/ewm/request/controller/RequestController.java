package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")

public class RequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable(value = "userId") Long userId,
                                              @RequestParam(name = "eventId") Long eventId) {
        return requestService.addNewRequest(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAllRequests(@PathVariable(value = "userId") Long userId) {
        return requestService.getRequestsByUserId(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(@PathVariable(value = "userId") Long userId,
                                                   @PathVariable(value = "requestId") Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
