package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest itemRequestShortDtoToItemRequest(ItemRequestShortDto itemRequestShortDto) {
        return ItemRequest.builder()
                .description(itemRequestShortDto.getDescription())
                .build();
    }

    public static ItemRequestDto itemRequestToRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requesterId(itemRequest.getRequester().getId() != null ? itemRequest.getRequester().getId() : null)
                .build();
    }

    public static ItemRequestDto itemRequestToRequestWithItems(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestShortDto itemRequestToItemRequestShortDto(ItemRequest itemRequest) {
        return ItemRequestShortDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequester().getId(),
                itemRequest.getCreated(), items);
    }

}

