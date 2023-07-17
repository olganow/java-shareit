package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemShortDto itemShortDto, Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    ItemDto updateItemById(ItemShortDto itemShortDto, Long itemId, Long userId);

    List<ItemDto> searchItemByText(String text);

    CommentDto addComment(CommentDto commentDto, Long userId, Long itemId);
}
