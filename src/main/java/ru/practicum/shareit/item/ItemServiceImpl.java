package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setOwner(user);
        log.info("Item with id = {} has been created", item.getId());
        return itemMapper.itemToItemDto(itemRepository.createItem(item, user));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.getAllItems(userId)) {
            items.add(itemMapper.itemToItemDto(item));
        }
        log.info("Get all items");
        return items;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Optional<Item> item = itemRepository.getItemById(itemId, userId);
        if (item.isEmpty()) {
            log.info("Item with id = {} doesn't exist", itemId);
            throw new NotFoundException("Item with id = " + itemId + "doesn't exist");
        }
        log.info("Item with id = {} is uploaded", itemId);
        return itemMapper.itemToItemDto(item.get());
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long itemId, Long userId) {
        Optional<Item> updatedItem = itemRepository.getItemById(itemId, userId);
        if (!updatedItem.get().getOwner().getId().equals(userId)) {
            log.info("Item with id = {} doesn't found", itemId);
            throw new NotFoundException("Item with id " + itemId + " doesn't found");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            updatedItem.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            updatedItem.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.get().setAvailable(itemDto.getAvailable());
        }
        log.info("Item with id = {} has been updated", itemId);
        return itemMapper.itemToItemDto(updatedItem.get());
    }

    @Override
    public List<ItemDto> searchItemByText(String text, Long userId) {
        if (text.isBlank()) {
            return List.of();
        }
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.searchItemByText(text, userId)) {
            items.add(itemMapper.itemToItemDto(item));
        }
        log.info("Item has been found");
        return items;
    }

    @Override
    public ItemDto removeItemById(Long itemId, Long userId) {
        userRepository.getUserById(userId);
        Optional<Item> optionalItem = itemRepository.getItemById(itemId, userId);

        if (!optionalItem.get().getOwner().getId().equals(userId)) {
            log.info("User with id ={} doesn't have item with id = {}, this item can't remove", userId, itemId);
            throw new NotFoundException("User with id = " + userId + " doesn't have item with id = " + itemId);
        }
        if (optionalItem.isEmpty()) {
            log.info("Item with id = {} doesn't found", itemId);
            throw new NotFoundException("Item with id " + itemId + " doesn't found");
        }
        log.info("Item with id = {} has been removed", itemId);
        optionalItem = itemRepository.removeItemById(itemId, userId);
        return itemMapper.itemToItemDto(optionalItem.get());
    }
}
