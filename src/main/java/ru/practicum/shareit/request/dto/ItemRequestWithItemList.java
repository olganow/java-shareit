package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemInRequest;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestWithItemList {
    private Long id;
    private String description;
    private Timestamp created;
    private List<ItemInRequest> items;
}