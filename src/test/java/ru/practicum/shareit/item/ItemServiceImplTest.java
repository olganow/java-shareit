
package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.item.dto.ItemMapper.itemDtoToItem;
import static ru.practicum.shareit.item.dto.ItemMapper.itemDtoToItemShortDto;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;
    private User user2;
    private Item item;
    private ItemDto itemDto;
    ItemShortDto itemShortDto;
    private ItemRequest itemRequest;
    private Comment comment;
    private CommentDto commentDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Name")
                .email("username@gmail.com")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("Name2")
                .email("user2name@gmail.com")
                .build();
        item = Item.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(user)
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getName()))
                .build();
        itemShortDto = new ItemShortDto(itemDto.getId(), itemDto.getName(),
                itemDto.getDescription(), true, itemDto.getRequestId());
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Item request description")
                .requester(user2)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(user2)
                .status(Status.WAITING)
                .build();
        comment = Comment.builder()
                .id(1L)
                .author(user2)
                .item(item)
                .text("Comment to item")
                .created(LocalDateTime.now())
                .build();
        commentDto = CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .itemId(item.getId())
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    public void createItemTest() {
        Long userId = user.getId();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemDto actualDto = itemService.createItem(itemShortDto, userId);

        assertEquals(itemDto.getId(), actualDto.getId());
        assertEquals(itemDto.getName(), actualDto.getName());
        assertEquals(itemDto.getDescription(), actualDto.getDescription());
        assertEquals(itemDto.getRequestId(), actualDto.getRequestId());
        assertThat(itemDto).hasFieldOrProperty("id");
    }


    @Test
    void createItemWithUserNotFoundExceptionValidationTest() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.createItem(itemShortDto, userId));

        verify(itemRequestRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
    }


    @Test
    void updateItemByIdNewNameTest() {
        Long itemId = item.getId();
        Long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        userRepository.findById(userId);
        itemShortDto.setName("new_name");
        ItemDto itemDto = itemService.updateItemById(itemShortDto, itemId, userId);
        assertThat(itemDto.getName()).isEqualTo("new_name");
    }

    @Test
    void updateItemByIdNewAvailableTeat() {
        Long itemId = item.getId();
        Long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        userRepository.findById(userId);
        itemShortDto.setAvailable(false);
        ItemDto itemDto = itemService.updateItemById(itemShortDto, itemId, userId);
        assertThat(itemDto.getAvailable().equals(false));
    }

    @Test
    void updateItemByIdNewDescriptionTest() {
        Long itemId = item.getId();
        Long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        userRepository.findById(userId);
        itemShortDto.setDescription("Newest_description");
        ItemDto itemDto = itemService.updateItemById(itemShortDto, itemId, userId);
        assertThat(itemDto.getDescription().equals("Newest_description"));
    }

    @Test
    void renewalItemByNotOwner() {
        Long itemId = item.getId();
        Long userId = null;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> itemService.updateItemById(itemShortDto, itemId, userId));

    }

    @Test
    public void updateItemByIdWithNullValidationTest() {
        Long itemId = item.getId();
        Long userId = user.getId();
        itemDto.setDescription(null);
        itemDto.setName(null);
        ItemDto expectedItem = itemDto;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(itemDtoToItem(itemDto));
        ItemDto actualItemDto = itemService.updateItemById(itemDtoToItemShortDto(itemDto), itemId, userId);

        assertEquals(expectedItem.getName(), actualItemDto.getName());
        assertEquals(expectedItem.getDescription(), actualItemDto.getDescription());
        assertEquals(expectedItem.getRequestId(), actualItemDto.getRequestId());
        verify(itemRepository, times(1)).save(any());
        verify(itemRequestRepository, never()).findById(1L);
    }

    @Test
    void searchItemByTextTest() {
        when(itemRepository.findByNameOrDescriptionAndAvailable("text")).thenReturn(List.of(item));
        List<ItemDto> expectedDtoList = List.of(ItemMapper.itemToItemDto(item));
        List<ItemDto> actualDtoList = itemService.searchItemByText("text");

        assertEquals(expectedDtoList.get(0).getId(), actualDtoList.get(0).getId());
        assertEquals(expectedDtoList.get(0).getDescription(), actualDtoList.get(0).getDescription());
        assertEquals(expectedDtoList.get(0).getName(), actualDtoList.get(0).getName());
        assertEquals(expectedDtoList.get(0).getOwner(), actualDtoList.get(0).getOwner());
    }


}
