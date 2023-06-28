package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item, User user);

    List<Item> getAllItems(Long userId);

    Optional<Item> getItemById(Long itemId, Long userId);

    void updateItemById(Item item, Long itemId, Long userId);

    List<Item> searchItemByText(String text, Long userId);

    Optional<Item> removeItemById(Long itemId, Long userId);
}
