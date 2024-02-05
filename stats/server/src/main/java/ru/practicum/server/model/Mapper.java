package ru.practicum.server.model;

import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseStatsDto;

public class Mapper {
    public static Stat toStatRequest(RequestStatsDto dto) {
        return new Stat(dto.getId(), dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }

    public static RequestStatsDto toRequestStatDto(Stat stat) {
        return new RequestStatsDto(stat.getId(), stat.getApp(), stat.getUri(), stat.getIp(), stat.getTimestamp());
    }

    public static ResponseStatsDto toResponseStatsDto(ResponseStat stat) {
        return new ResponseStatsDto(stat.getApp(), stat.getUri(), stat.getHits());
    }
}
