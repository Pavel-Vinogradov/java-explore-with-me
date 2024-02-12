package ru.practicum.ewm.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
