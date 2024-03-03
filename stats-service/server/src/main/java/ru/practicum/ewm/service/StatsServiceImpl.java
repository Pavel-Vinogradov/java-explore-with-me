package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.RequestStatsDto;
import ru.practicum.ewm.ResponseStatsDto;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.exception.ValidTimeException;
import ru.practicum.ewm.model.Mapper;
import ru.practicum.ewm.model.ResponseStat;
import ru.practicum.ewm.model.Stat;
import ru.practicum.ewm.repository.StatsRepository;

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
    public List<ResponseStatsDto> getStats(StatsRequestDto requestStatsDto) {
        checkValidTime(requestStatsDto.getStart(), requestStatsDto.getEnd());

        List<ResponseStat> stats;

        if (requestStatsDto.isUnique()) {
            stats = statsRepository.getStatByUrisAndTimeIsUnique(requestStatsDto.getUris(), requestStatsDto.getStart(), requestStatsDto.getEnd());
        } else {
            stats = statsRepository.getStatByUrisAndTime(requestStatsDto.getUris(), requestStatsDto.getStart(), requestStatsDto.getEnd(), requestStatsDto.getLimit());
        }

        return stats.stream().map(Mapper::toResponseStatsDto).collect(Collectors.toList());
    }

    private void checkValidTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new ValidTimeException("Время начала не может быть позже окончания мероприятия");
        }
    }
}
