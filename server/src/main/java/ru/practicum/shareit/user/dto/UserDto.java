package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 255)
    private String name;

    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotEmpty(groups = Marker.OnCreate.class)
    @Size(max = 512)
    private String email;
}