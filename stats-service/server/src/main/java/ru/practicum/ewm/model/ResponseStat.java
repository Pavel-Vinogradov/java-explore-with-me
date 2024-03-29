package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStat {
    private String app;
    private String uri;
    private long hits;
}
