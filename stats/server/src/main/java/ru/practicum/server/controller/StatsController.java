package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.server.dto.StatsRequestDto;
import ru.practicum.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public RequestStatsDto postStat(@RequestBody RequestStatsDto requestStatsDto) {
        return statsService.postStat(requestStatsDto);
    }

    @GetMapping("/stats")
    public List<ResponseStatsDto> getStat(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                          @RequestParam(name = "uris", required = false) ArrayList<String> uris,
                                          @RequestParam(value = "unique", defaultValue = "false") boolean unique,
                                          @RequestParam(value = "limit", defaultValue = "20") int limit) {
        StatsRequestDto statsRequestDto =
                StatsRequestDto.builder()
                        .start(start)
                        .end(end)
                        .uris(uris)
                        .unique(unique)
                        .limit(limit)
                        .build();
        return statsService.getStats(statsRequestDto);
    }
}
