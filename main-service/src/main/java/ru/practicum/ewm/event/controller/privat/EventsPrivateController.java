package ru.practicum.ewm.event.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventsPrivateController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(@PathVariable @Positive int userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос событий юзера id = {}, параметры from = {}, size = {}", userId, from, size);
        return eventService.getEventsUser(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@PathVariable @Positive int userId,
                                              @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос на создание события от юзера id = {}", userId);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable long userId,
                                               @PathVariable long eventId) {
        log.info("Запрос события id = {} юзера id = {}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable @Positive long userId,
                                              @PathVariable @Positive  long eventId,
                                              @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Обновление события id = {} юзером id = {}", eventId, userId);
        return eventService.updateEventUser(userId, eventId, updateEventUserRequest);
    }


}
