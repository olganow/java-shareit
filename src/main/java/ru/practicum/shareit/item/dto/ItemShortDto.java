package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemShortDto {
    private Long id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Description can't be blank")
    private String description;

    @NotNull
    private Boolean available;
}
