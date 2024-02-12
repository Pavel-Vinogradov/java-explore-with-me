package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatClient;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ex.DateTimeValidateException;
import ru.practicum.ewm.exception.ex.EventNotFountException;
import ru.practicum.ewm.exception.ex.EventPublishedException;
import ru.practicum.ewm.exception.ex.LocalDataTimeParseException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.Page;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventStorage;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final StatClient statClient;

    public ResponseEntity<Object> createEvent(long userId, NewEventDto newEventDto) {
        User user = userService.checkExistsUser(userId);
        Category category = categoryService.checkExistCategory(newEventDto.getCategory());
        Location location = locationService.createLocation(LocationMapper.toLocation(newEventDto.getLocation()));

        Event event = EventMapper.toEvent(newEventDto);

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new DateTimeValidateException("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value:");
        }

        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(StateEvent.PENDING);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventStorage.save(event));
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getEventsUser(int userId, int from, int size) {
        userService.checkExistsUser(userId);
        PageRequest page = Page.createPageRequest(from, size);
        List<EventShortDto> events = eventStorage.findAllByInitiatorId(userId, page).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventById(long userId, long eventId) {
        userService.checkExistsUser(userId);
        Event event = checkExistsEvent(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new EventNotFountException("Event with id= " + eventId + " was not found");
        }

        return new ResponseEntity<>(EventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    public Event checkExistsEvent(long eventId) {
        Optional<Event> optionalEvent = eventStorage.findById(eventId);

        if (optionalEvent.isEmpty()) {
            throw new EventNotFountException("Event with id= " + eventId + " was not found");
        }

        return optionalEvent.get();
    }

    public ResponseEntity<Object> updateEventUser(long userId, long eventId,
                                              UpdateEventUserRequest updateEventUserRequest) {
        userService.checkExistsUser(userId);

        Event event = checkExistsEvent(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new EventNotFountException("Event with id= " + eventId + " was not found");
        }

        if (event.getState().equals(StateEvent.PUBLISHED)) {
            throw new EventPublishedException("Only pending or canceled events can be changed.");
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryService.checkExistCategory(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateTimeValidateException("Field: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила. Value:");
            } else {
                event.setEventDate(updateEventUserRequest.getEventDate());
            }
        }

        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationService.createLocation(
                    LocationMapper.toLocation(updateEventUserRequest.getLocation()));
            event.setLocation(location);
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getStateAction() != null &&
                updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            event.setState(StateEvent.CANCELED);
        } else if (updateEventUserRequest.getStateAction() != null &&
                updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(StateEvent.PENDING);
        }

        Event eventUpdate = eventStorage.save(event);
        return new ResponseEntity<>(EventMapper.toEventFullDto(eventUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Object> adminSearchEvents(List<Long> users, List<StateEvent> states, List<Long> idsCategory,
                   String rangeStart, String rangeEnd, int from, int size) {

        PageRequest page = Page.createPageRequest(from, size);
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = parseDataTime(rangeStart);
        }

        if (rangeEnd != null) {
            end = parseDataTime(rangeEnd);
        }

        List<EventFullDto> events =
                eventStorage.adminSearchEvents(users, states, idsCategory, start, end, page).stream()
                             .map(EventMapper::toEventFullDto)
                             .collect(Collectors.toList());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateEventAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkExistsEvent(eventId);

        if (event.getState().equals(StateEvent.PUBLISHED) || event.getState().equals(StateEvent.CANCELED)) {
            throw new EventPublishedException("Only pending or canceled events can be changed.");
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryService.checkExistCategory(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateTimeValidateException("Field: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила. Value:");
            } else {
                event.setEventDate(updateEventAdminRequest.getEventDate());
            }
        }

        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationService.createLocation(
                    LocationMapper.toLocation(updateEventAdminRequest.getLocation()));
            event.setLocation(location);
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        if (updateEventAdminRequest.getStateAction() != null &&
                updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            event.setState(StateEvent.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (updateEventAdminRequest.getStateAction() != null &&
                updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
            event.setState(StateEvent.CANCELED);
        }

        Event eventUpdate = eventStorage.save(event);
        return new ResponseEntity<>(EventMapper.toEventFullDto(eventUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventPublished(long eventId, HttpServletRequest request) {
        Event event = checkExistsEvent(eventId);

        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new EventNotFountException("Event with id= " + eventId + " was not found");
        }

        createHit(request);
        event.setViews((int) getView(eventId));
        return new ResponseEntity<>(EventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    public void changeConfirmedRequests(long eventId, boolean increment) {
        Event event = checkExistsEvent(eventId);

        if (increment) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }

        eventStorage.save(event);
    }

    private LocalDateTime parseDataTime(String value) {
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new LocalDataTimeParseException("Дата должна быть в формате yyyy-MM-dd HH:mm:ss");
        }
    }

    public ResponseEntity<Object> getEventsPublished(String text, List<Integer> categories, Boolean paid,
                                                     String rangeStart, String rangeEnd, boolean onlyAvailable,
                                                     SortEvent sort, int from, int size, HttpServletRequest request) {

        PageRequest page = Page.createPageRequest(from, size);
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
        }

        if (rangeStart != null) {
            start = parseDataTime(rangeStart);
        }

        if (rangeEnd != null) {
            end = parseDataTime(rangeEnd);
        }

        if (start != null && end != null && start.isAfter(end)) {
            throw new DateTimeValidateException("Передана некорректные даты.");
        }

        List<Event> events = eventStorage.publicSearchAllEvents(StateEvent.PUBLISHED, text, categories,
                paid, start, end, page);

        List<EventFullDto> eventFullDtos;

        if (onlyAvailable) {
            eventFullDtos = events.stream()
                    .filter(x -> x.getParticipantLimit() == 0 || x.getParticipantLimit() > x.getConfirmedRequests())
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        } else {
            eventFullDtos = events.stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }

        createHit(request);

        for (EventFullDto eventFullDto : eventFullDtos) {
            eventFullDto.setViews((int) getView(eventFullDto.getId()));
        }

        if (sort == SortEvent.EVENT_DATE) {
            List<EventFullDto> eventFullDtosSorted = eventFullDtos.stream()
                    .sorted(Comparator.comparing(EventFullDto::getEventDate))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(eventFullDtosSorted, HttpStatus.OK);

        } else if (sort == SortEvent.VIEWS) {
            List<EventFullDto> eventFullDtosSorted = eventFullDtos.stream()
                    .sorted(Comparator.comparingInt(EventFullDto::getViews))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(eventFullDtosSorted, HttpStatus.OK);
        }

        return new ResponseEntity<>(eventFullDtos, HttpStatus.OK);
    }

    private void createHit(HttpServletRequest request) {
        statClient.createStat(HitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(FORMATTER.format(LocalDateTime.now()))
                .build());
    }

    private long getView(long eventId) {
        List<String> uris = List.of(String.format("/events/%s", eventId));

        ResponseEntity<Object> objectStatsDto = statClient.getStat(LocalDateTime.now().minusYears(1).format(FORMATTER),
                LocalDateTime.now().format(FORMATTER), uris, true);

        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, StatsDto.class);
        List<StatsDto> statsDtos = mapper.convertValue(objectStatsDto.getBody(), type);

        return !statsDtos.isEmpty() ? statsDtos.get(0).getHits() : 0;
    }
}
