package ru.practicum.shareitgateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.util.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    @NotBlank(groups = {Marker.OnCreate.class})
    @Size(groups = {Marker.OnCreate.class}, max = 255)
    private String name;

    @NotBlank(groups = {Marker.OnCreate.class})
    @Size(groups = {Marker.OnCreate.class}, max = 512)
    private String description;

    @NotNull(groups = {Marker.OnCreate.class})
    private Boolean available;

    private Long requestId;
}
