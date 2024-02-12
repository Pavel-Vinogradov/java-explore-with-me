package ru.practicum.ewm.event.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.StateEvent;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventsAdminController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> searchEvents(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<StateEvent> states,
                                               @RequestParam(required = false) List<Long> idsCategory,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на поиск событий.");
        return eventService.adminSearchEvents(users, states, idsCategory, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable @Positive int eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Обновление события id = {} админом", eventId);
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }
}
