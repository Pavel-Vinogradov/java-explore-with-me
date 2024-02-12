package ru.practicum.ewm.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private int id;

    @NotBlank
    @Size(min = 1, max = 50, message = "Длина имени должна быть от 1 до 50 символов.")
    private String name;
}
