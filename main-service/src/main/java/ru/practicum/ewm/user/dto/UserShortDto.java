package ru.practicum.ewm.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private long id;
    private String name;
}
