package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @Email
    @NotEmpty(message = "Email can't be empty")
    private String email;
}