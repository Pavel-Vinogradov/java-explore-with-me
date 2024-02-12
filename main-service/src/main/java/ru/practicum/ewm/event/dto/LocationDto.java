package ru.practicum.ewm.event.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @Min(-90)
    @Max(90)
    private float lat;

    @Min(-180)
    @Max(180)
    private float lon;
}