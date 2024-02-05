package ru.practicum.server.service;

import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.server.dto.StatsRequestDto;

import java.util.List;

public interface StatsService {
    RequestStatsDto postStat(RequestStatsDto requestStatsDto);

    List<ResponseStatsDto> getStats(StatsRequestDto statsRequestDto);

}
