package ru.practicum.ewm.event.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventsService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventsPrivateController {
    private final EventsService eventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventsService.postEvent(userId, newEventDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventForOwner(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventsService.getEventForOwner(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getFullEventForOwner(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventsService.getFullEventForOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventOwner(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestBody @Valid UpdateEventUserRequest eventUserRequest) {
        return eventsService.updateEventOwner(userId, eventId, eventUserRequest);
    }


    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestByEventFromOwner(@PathVariable(value = "userId") Long userId,
                                                                       @PathVariable(value = "eventId") Long eventId) {
        return eventsService.getAllParticipationRequestsFromEventByOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestFromOwner(@PathVariable(value = "userId") Long userId,
                                                                       @PathVariable(value = "eventId") Long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequest inputUpdate) {
        return eventsService.updateStatusRequest(userId, eventId, inputUpdate);
    }


}
