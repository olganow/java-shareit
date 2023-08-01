package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequestShortDto {
    private Long id;

    @NotBlank(message = "Description can't be blank")
    @Size(max = 512)
    private String description;
}