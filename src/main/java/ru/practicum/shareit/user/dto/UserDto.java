package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = Marker.OnCreate.class)
    private String name;

    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotEmpty(groups = Marker.OnCreate.class)
    private String email;
}