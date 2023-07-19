package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequestShortDto {
    private Long id;

    @NotBlank(message = "Description can't be blank")
    private String description;

    private Long requesterId;

    private Timestamp created;
}