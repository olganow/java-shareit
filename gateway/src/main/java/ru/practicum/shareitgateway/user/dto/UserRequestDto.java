package ru.practicum.shareitgateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareitgateway.util.Marker;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(groups = Marker.OnCreate.class)
    @Size(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, max = 255)
    private String name;

    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, max = 512)
    private String email;
}
