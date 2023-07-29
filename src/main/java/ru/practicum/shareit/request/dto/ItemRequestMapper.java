package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequest;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest itemRequestShortDtoToItemRequest(ItemRequestShortDto itemRequestShortDto) {
        return ItemRequest.builder()
                .description(itemRequestShortDto.getDescription())
                .created(itemRequestShortDto.getCreated())
                .build();
    }

    public static ItemRequestDto itemRequestToRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requester(itemRequest.getRequester() != null ? itemRequest.getRequester() : null)
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
                .requesterId(itemRequest.getRequester().getId() != null ? itemRequest.getRequester().getId() : null)
                .created(itemRequest.getCreated())
                .build();
    }

}

