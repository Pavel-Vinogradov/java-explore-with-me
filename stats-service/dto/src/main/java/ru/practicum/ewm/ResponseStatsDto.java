package ru.practicum.ewm;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@Data
public class ResponseStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
