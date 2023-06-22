package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Description can't be blank")
    private String description;
    private Boolean available;
    @NotNull
    private User owner;
    private ItemRequest request;
}