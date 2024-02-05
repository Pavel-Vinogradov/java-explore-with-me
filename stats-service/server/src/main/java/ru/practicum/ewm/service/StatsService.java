package ru.practicum.ewm.service;

import ru.practicum.ewm.RequestStatsDto;
import ru.practicum.ewm.ResponseStatsDto;
import ru.practicum.ewm.dto.StatsRequestDto;

import java.util.List;

public interface StatsService {
    RequestStatsDto postStat(RequestStatsDto requestStatsDto);

    List<ResponseStatsDto> getStats(StatsRequestDto statsRequestDto);

}