package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть в диапазоне 20-2000.")
    private String annotation;

    @NotNull
    @Positive(message = "Значение категории не может быть отрицательным.")
    private Integer category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Длина описании должна быть в диапазоне 20-7000.")
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120, message = "Длина аннотации должна быть в диапазоне 3-120.")
    private String title;
}
