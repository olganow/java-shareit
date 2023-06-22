package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotEmpty(message = "Email can't be empty")
    private String email;
}
