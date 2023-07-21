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

    public static ItemRequestWithItemList itemRequestToRequestWithItems(ItemRequest itemRequest) {
        return ItemRequestWithItemList.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }
}
