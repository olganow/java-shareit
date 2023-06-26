package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    ItemDto updateItemById(ItemDto itemDto, Long itemId, Long userId);

    List<ItemDto> searchItemByText(String text, Long userId);

    ItemDto removeItemById(Long itemId, Long userId);
}
