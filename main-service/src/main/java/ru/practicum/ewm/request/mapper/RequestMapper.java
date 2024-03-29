package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .requester(request.getId())
                .status(request.getStatus())
                .build();
    }

}
