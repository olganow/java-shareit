package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 512)
    private String email;
}