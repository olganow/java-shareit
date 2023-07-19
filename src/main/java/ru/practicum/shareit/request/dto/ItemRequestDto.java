package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Description can't be blank")
    private String description;

    private User requester;

    private Timestamp created;
}