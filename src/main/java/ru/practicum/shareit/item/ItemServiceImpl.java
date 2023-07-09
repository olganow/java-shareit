
package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.item.ItemMapper.itemDtoToItem;
import static ru.practicum.shareit.item.ItemMapper.itemToItemDto;
import static ru.practicum.shareit.item.comment.CommentMapper.commentToCommentDto;

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
        log.info("Item with text = '{}' has been found", text);
        return items;
    }


    public List<CommentDto> getComments(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(CommentMapper.commentToCommentDto(comment));
        }
        log.info("Get all comments");
        return commentDto;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment comment = Comment
                .builder()
                .text(commentDto.getText())
                .build();
        comment.setAuthor(userMapper.userDtotoUser(userService.getUserById(userId)));

        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item  hasn't be found"))));

        comment.setAuthor(userMapper.userDtotoUser(userService.getUserById(userId)));
        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("Comment can't be created");
        }
        comment.setCreated(LocalDateTime.now());
        log.info("Comment has been added");
        return commentToCommentDto(commentRepository.save(comment));
    }

    private ItemDto setBookings(ItemDto itemDto, Long userId) {
        if (itemDto.getOwner().getId().equals(userId)) {
            itemDto.setLastBooking(
                    bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemDto.getId(),
                                    LocalDateTime.now(), Status.APPROVED).map(BookingMapper::bookingToItemBookingDto)
                            .orElse(null));

            itemDto.setNextBooking(
                    bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemDto.getId(),
                                    LocalDateTime.now(), Status.APPROVED).map(BookingMapper::bookingToItemBookingDto)
                            .orElse(null));

            return itemDto;
        }
        log.info("Booking has been set");
        return itemDto;
    }

}
