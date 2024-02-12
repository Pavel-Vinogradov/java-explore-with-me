package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.StateEvent;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ex.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestStorage;
    private final UserService userService;
    private final EventService eventService;

    public ResponseEntity<Object> createRequest(long userId, long eventId) {
        User user = userService.checkExistsUser(userId);
        Event event = eventService.checkExistsEvent(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new InitiatorEventException("Нельзя создавать заявку на собственное событие.");
        }

        checkForRepeatedRequest(userId, eventId);

        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new EventPublishedException("Нельзя участвовать в неопубликованном событии.");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .build();

        if (event.getParticipantLimit() == 0) {
            eventService.changeConfirmedRequests(eventId, true);
            request.setStatus(RequestStatus.CONFIRMED);
            return new ResponseEntity<>(RequestMapper.toParticipationRequestDto(requestStorage.save(request)),
                    HttpStatus.CREATED);
        }

        List<RequestStatus> requestStatuses = List.of(RequestStatus.CANCELED,
                RequestStatus.REJECTED, RequestStatus.PENDING);

        long countRequestEvent = requestStorage.countByEventIdAndStatusIsNotIn(eventId, requestStatuses);

        if (event.getParticipantLimit() != 0 && countRequestEvent >= event.getParticipantLimit()) {
            throw new ParticipantLimitException("Превышен лимит заявок на событие.");
        }

        if (event.isRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            eventService.changeConfirmedRequests(eventId, true);
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return new ResponseEntity<>(RequestMapper.toParticipationRequestDto(requestStorage.save(request)),
                HttpStatus.CREATED);
    }

    public ResponseEntity<Object> cancelRequest(long userId, long requestId) {
        userService.checkExistsUser(userId);
        Request request = checkExistsRequest(requestId);

        if (!(request.getRequester().getId() == userId)) {
            throw new RequestNotFountException("Request with id = " + requestId + " was not found");
        }

        if (request.getStatus().equals(RequestStatus.CANCELED) || request.getStatus().equals(RequestStatus.REJECTED)) {
            throw new RequestNotFountException("Request with id = " + requestId + " was not found");
        }

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            eventService.changeConfirmedRequests(request.getEvent().getId(), false);
        }

        request.setStatus(RequestStatus.CANCELED);

        return new ResponseEntity<>(RequestMapper.toParticipationRequestDto(requestStorage.save(request)),
                HttpStatus.OK);
    }

    public ResponseEntity<Object> getRequests(long userId) {
        userService.checkExistsUser(userId);
        List<ParticipationRequestDto> requests = requestStorage.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public ResponseEntity<Object> getRequestsInEvent(long userId, long eventId) {
        userService.checkExistsUser(userId);
        Event event = eventService.checkExistsEvent(eventId);

        if (!(event.getInitiator().getId() == userId)) {
            throw new EventNotFountException("Event with id = " + eventId + " was not found");
        }

        List<Request> requests = requestStorage.findAllByEventId(eventId);

        List<ParticipationRequestDto> requestsDto = requests.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(requestsDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateStatusRequests(long userId, long eventId,
                             EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        RequestStatus updateStatus = eventRequestStatusUpdateRequest.getStatus();

        userService.checkExistsUser(userId);
        Event event = eventService.checkExistsEvent(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new EventNotFountException("Event with " + eventId + " was not found.");
        }

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new UpdateStatusRequestEventException("Подтверждение заявок не требуется.");
        }

        List<Request> requests = eventRequestStatusUpdateRequest.getRequestIds().stream()
                .map(this::checkExistsRequest)
                .collect(Collectors.toList());

        for (Request request : requests) {

            Event checkEvent = eventService.checkExistsEvent(eventId);

            if (checkEvent.getConfirmedRequests() >= checkEvent.getParticipantLimit()) {
                throw new EventLimitConfirmedException("The participant limit has been reached");
            }

            if (request.getStatus().equals(updateStatus)) {
                throw new UpdateStatusRequestEventException("Статус " +
                        updateStatus + " уже применен.");
            }

            request.setStatus(updateStatus);
            requestStorage.save(request);

            if (updateStatus.equals(RequestStatus.CONFIRMED)) {
                eventService.changeConfirmedRequests(eventId, true);
            }
        }

        List<ParticipationRequestDto> requestDto = requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        if (updateStatus.equals(RequestStatus.CONFIRMED)) {
            eventRequestStatusUpdateResult.setConfirmedRequests(requestDto);
        } else {
            eventRequestStatusUpdateResult.setRejectedRequests(requestDto);
        }

        return new ResponseEntity<>(eventRequestStatusUpdateResult, HttpStatus.OK);
    }

    private Request checkExistsRequest(long requestId) {
        Optional<Request> optionalRequest = requestStorage.findById(requestId);

        if (optionalRequest.isEmpty()) {
            throw new RequestNotFountException("Request with id = " + requestId + " was not found");
        }

        return optionalRequest.get();
    }

    private void checkForRepeatedRequest(long userId, long eventId) {

        List<RequestStatus> requestStatuses = List.of(RequestStatus.CANCELED,
                RequestStatus.REJECTED);

        Request request =
                requestStorage.findByRequesterIdAndEventIdAndStatusIsNotIn(userId, eventId, requestStatuses);

        if (request != null) {
            throw new RepeatedRequestException("Заявка уже существует.");
        }
    }
}
