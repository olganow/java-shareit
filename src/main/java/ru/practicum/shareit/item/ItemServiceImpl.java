
package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.itemDtoToItem;
import static ru.practicum.shareit.item.ItemMapper.itemToItemDto;
import static ru.practicum.shareit.item.comment.CommentMapper.commentToCommentDto;
import static ru.practicum.shareit.user.UserMapper.userDtotoUser;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;


    public ItemDto createItem(ItemDto itemDto, Long userId) {
        itemDto.setOwner(userDtotoUser(userService.getUserById(userId)));
        log.info("Item with id = {} has been created", itemDto.getId());
        return itemToItemDto(itemRepository.save(itemDtoToItem(itemDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getAllItems(Long userId) {
        List<Item> itemList = (List<Item>) itemRepository.findAllByOwnerId(userId);
        List<ItemDto> items = itemList.stream().map(ItemMapper::itemToItemDto).collect(Collectors.toList());

        items.forEach(item -> {
            item.setComments(getComments(item.getId()));
            setBookings(item, userId);
        });

        log.info("Get all items");
        return items;
    }

    @Transactional(readOnly = true)
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


    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItemByText(String stringText) {
        String text = stringText.toLowerCase();
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByNameOrDescriptionAndAvailable(text)
                .stream()
                .peek(item -> setBookings(itemToItemDto(item), item.getOwner().getId()))
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }


    private List<CommentDto> getComments(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        log.info("Get all comments");
        return comments.stream()
                .map(CommentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment comment = Comment
                .builder()
                .text(commentDto.getText())
                .build();
        comment.setAuthor(userDtotoUser(userService.getUserById(userId)));

        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item  hasn't be found"))));

        comment.setAuthor(userDtotoUser(userService.getUserById(userId)));
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
