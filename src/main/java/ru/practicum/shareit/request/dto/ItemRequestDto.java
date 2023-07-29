package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemInRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

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
    private List<ItemInRequest> items;

}