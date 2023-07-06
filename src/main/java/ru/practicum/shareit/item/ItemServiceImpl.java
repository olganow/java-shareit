package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.item.ItemMapper.itemDtoToItem;
import static ru.practicum.shareit.item.ItemMapper.itemToItemDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        itemDto.setOwner(userMapper.userDtotoUser(userService.getUserById(userId)));
        log.info("Item with id = {} has been created", itemDto.getId());
        return itemToItemDto(itemRepository.save(itemDtoToItem(itemDto)));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.findAllByOwnerId(userId)) {
            items.add(itemToItemDto(item));
        }
        for (ItemDto item : items) {
            item.setComments(getComments(item.getId()));
            setBookings(item, userId);
        }
        log.info("Get all items");
        return items;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        ItemDto item = itemToItemDto(itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Item has not found")));
        item.setComments(getComments(item.getId()));
        setBookings(item, userId);
        log.info("Item with id = {} is uploaded", itemId);
        return item;
    }


    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long itemId, Long userId) {
        Optional<Item> updatedItem = itemRepository.findById(itemId);
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
        return itemToItemDto(itemRepository.save(updatedItem.get()));
    }


    @Override
    public List<ItemDto> searchItemByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.findByNameOrDescriptionAndAvailable(text)) {
            items.add(itemToItemDto(item));
        }
        log.info("Item has been found");
        return items;
    }


    @Transactional
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        return null;
    }

    private ItemDto setBookings(ItemDto itemDto, Long userId) {
        return itemDto;
    }

    public List<CommentDto> getComments(Long itemId) {
        return null;
    }
}
