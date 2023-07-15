package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemMapper {

    public static ItemDto itemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(new ItemDto.Owner(item.getOwner().getId(), item.getOwner().getName(), item.getOwner().getEmail()))
                .comments(new ArrayList<>())
                .build();
    }

    public static ItemDto itemToItemDtoWithComments(Item item, List comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(new ItemDto.Owner(item.getOwner().getId(), item.getOwner().getName(), item.getOwner().getEmail()))
                .comments(comments)
                .build();
    }

    public static Item itemDtoToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(new User(itemDto.getOwner().getId(), itemDto.getOwner().getName(), itemDto.getOwner().getEmail()))
                .build();
    }

    public static ItemDto itemShortDtoToItemDto(ItemShortDto itemShortDto) {
        return ItemDto.builder()
                .id(itemShortDto.getId())
                .name(itemShortDto.getName())
                .description(itemShortDto.getDescription())
                .available(itemShortDto.getAvailable())
                .comments(new ArrayList<>())
                .build();
    }

    public static Item itemShortDtoToItem(ItemShortDto itemShortDto) {
        return Item.builder()
                .id(itemShortDto.getId())
                .name(itemShortDto.getName())
                .description(itemShortDto.getDescription())
                .available(itemShortDto.getAvailable())
                .build();
    }
}