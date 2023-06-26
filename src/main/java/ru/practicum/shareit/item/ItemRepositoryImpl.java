package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item createItem(Item item, User user) {
        if (item.getId() == null) {
            item.setId(++id);
        }
        items.put(item.getId(), item);
        log.info("Item with id = {} has been added to Repository", item.getId());
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long itemId, Long userId) {
        if (!items.containsKey(itemId)) {
            return Optional.empty();
        }
        log.info("Item with id = {} is uploaded from Repository", itemId);
        return Optional.of(items.get(itemId));
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId().equals(userId)) {
                itemList.add(item);
            }
        }
        log.info("Get all items from Repository");
        return itemList;
    }

    @Override
    public Item updateItemById(Item item, Long itemId, Long userId) {
        items.put(itemId, item);
        log.info("Item with id = {} has been updated in Repository", itemId);
        return getItemById(itemId, userId).get();
    }

    @Override
    public List<Item> searchItemByText(String text, Long userId) {
        List<Item> itemList = new ArrayList<>();
        boolean nameValidation;
        boolean descriptionValidation;

        if (text.isEmpty()) {
            return itemList;
        }
        for (Item item : items.values()) {
            nameValidation = item.getName().contains(text);
            descriptionValidation = item.getDescription().contains(text);
            if ((nameValidation || descriptionValidation) && item.getAvailable()) {
                itemList.add(item);
            }
        }
        log.info("Item has been found in Repository");
        return itemList;
    }

    @Override
    public Optional<Item> removeItemById(Long itemId, Long userId) {
        Optional<Item> item = getItemById(itemId, userId);
        if (item.isEmpty()) {
            log.info("Item with id = {} doesn't found in Repository", itemId);
            return Optional.empty();
        }
        items.remove(itemId);
        log.info("Item with id = {} has been removed in Repository", itemId);
        return item;
    }
}