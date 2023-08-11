package ru.practicum.shareitgateway.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.util.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank(groups = {Marker.OnCreate.class})
    @Size(groups = {Marker.OnCreate.class}, max = 512)
    private String description;
}
