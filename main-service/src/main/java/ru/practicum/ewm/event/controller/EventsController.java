package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.ParamsSearchForAdmin;
import ru.practicum.ewm.event.model.SearchParamsForEvents;
import ru.practicum.ewm.event.service.EventsService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class EventsController {
    private final EventsService eventsService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventsService.postEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventForOwner(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") /*@PositiveOrZero*/ Integer from,
                                                @RequestParam(defaultValue = "10") /*@Positive*/ Integer size) {
        return eventsService.getEventForOwner(userId, from, size);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getFullEventForOwner(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventsService.getFullEventForOwner(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventOwner(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestBody @Valid UpdateEventUserRequest eventUserRequest) {
        return eventsService.updateEventOwner(userId, eventId, eventUserRequest);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchEventsFromAdmin(@Valid ParamsSearchForAdmin paramsSearch) {
        return eventsService.getAllEventFromAdmin(paramsSearch);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable(value = "eventId") Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest inputUpdate) {
        return eventsService.updateEventFromAdmin(eventId, inputUpdate);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestByEventFromOwner(@PathVariable(value = "userId") Long userId,
                                                                       @PathVariable(value = "eventId") Long eventId) {
        return eventsService.getAllParticipationRequestsFromEventByOwner(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestFromOwner(@PathVariable(value = "userId") Long userId,
                                                                       @PathVariable(value = "eventId") Long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequest inputUpdate) {
        return eventsService.updateStatusRequest(userId, eventId, inputUpdate);
    }

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@Valid SearchParamsForEvents searchParamsForEvents,
                                            HttpServletRequest request) {
        return eventsService.getAllEventFromPublic(searchParamsForEvents, request);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "eventId") Long eventId,
                                     HttpServletRequest request) {
        return eventsService.getEventById(eventId, request);
    }
}
