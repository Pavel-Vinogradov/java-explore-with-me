package ru.practicum.ewm.comment.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {
    private Long commentId;
    @NotBlank
    @Size(min = 2, max = 2000)
    private String text;
}