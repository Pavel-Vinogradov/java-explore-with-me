package ru.practicum.ewm.event.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.SortEvent;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventsPublicController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(@RequestParam(defaultValue = "") String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(required = false) SortEvent sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size,
                                            HttpServletRequest request) {
        log.info("Публичный запрос списка событий.");
        return eventService.getEventsPublished(text, categories, paid, rangeStart, rangeEnd,
                                               onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable @Positive long id,
                                               HttpServletRequest request) {
        log.info("Публичный запрос информации о событии.");
        return eventService.getEventPublished(id, request);
    }
}
