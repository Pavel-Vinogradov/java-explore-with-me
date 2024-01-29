package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.server.dto.StatsRequestDto;
import ru.practicum.server.exception.ValidTimeException;
import ru.practicum.server.model.Mapper;
import ru.practicum.server.model.ResponseStat;
import ru.practicum.server.model.Stat;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public RequestStatsDto postStat(RequestStatsDto requestStatsDto) {
        Stat stat = Mapper.toStatRequest(requestStatsDto);
        return Mapper.toRequestStatDto(statsRepository.save(stat));
    }

    @Override
    public List<ResponseStatsDto> getStats(StatsRequestDto statsRequestDto) {
        checkValidTime(statsRequestDto.getStart(), statsRequestDto.getEnd());

        List<ResponseStat> stats;

        if (statsRequestDto.isUnique()) {
            stats = statsRepository.getStatByUrisAndTimeIsUnique(statsRequestDto.getUris(), statsRequestDto.getStart(), statsRequestDto.getEnd(),statsRequestDto.getLimit());
        } else {
            stats = statsRepository.getStatByUrisAndTime(statsRequestDto.getUris(), statsRequestDto.getStart(), statsRequestDto.getEnd(),statsRequestDto.getLimit());
        }

        return stats.stream().map(Mapper::toResponseStatsDto).collect(Collectors.toList());
    }

    private void checkValidTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new ValidTimeException("Время начала не может быть позже или равно времени окончания мероприятия");
        }
    }
}
