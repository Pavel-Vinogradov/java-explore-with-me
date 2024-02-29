package ru.practicum.ewm.event.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.ParamsSearchForAdmin;
import ru.practicum.ewm.event.service.EventsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventsAdminController {
    private final EventsService eventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchEventsFromAdmin(@Valid ParamsSearchForAdmin paramsSearch) {
        return eventsService.getAllEventFromAdmin(paramsSearch);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable(value = "eventId") Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest inputUpdate) {
        return eventsService.updateEventFromAdmin(eventId, inputUpdate);
    }

}
