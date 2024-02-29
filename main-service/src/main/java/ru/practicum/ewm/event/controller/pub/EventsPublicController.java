package ru.practicum.ewm.event.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.SearchParamsForEvents;
import ru.practicum.ewm.event.service.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventsPublicController {
    private final EventsService eventsService;

    @GetMapping
    public List<EventShortDto> getAllEvents(@Valid SearchParamsForEvents searchParamsForEvents,
                                            HttpServletRequest request) {
        return eventsService.getAllEventFromPublic(searchParamsForEvents, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "eventId") Long eventId,
                                     HttpServletRequest request) {
        return eventsService.getEventById(eventId, request);
    }
}
