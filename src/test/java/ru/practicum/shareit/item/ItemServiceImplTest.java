package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemList;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private User user;
    private User userWithBooking;
    private Item item;
    private ItemDto itemDto;
    private ItemRequest itemRequest;
    private ItemRequestShortDto itemRequestShortDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("username@test.testz")
                .build();

        userWithBooking = User.builder()
                .id(2L)
                .name("User_WithBooking_Name")
                .email("userwithbooking@test.testz")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .owner(user)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()))
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Item request description")
                .requester(userWithBooking)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        itemRequestShortDto = ItemRequestShortDto.builder()
                .id(1L)
                .description("Item request short dto description")
                .requesterId(user.getId())
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @Test
    void createRequestTest() {
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(Mockito.any())).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(userId, itemRequestShortDto);

        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequestShortDto.getDescription());
    }

    @Test
    void createRequestWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(userId, itemRequestShortDto));

        Mockito.verify(itemRequestRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void getByIdTest() {
        Long userId = user.getId();
        Long requestId = 1L;
        ItemRequest itemRequest = new ItemRequest(1L, "Item_description", user, null);
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(Mockito.anyLong())).thenReturn(List.of());
        ItemRequestWithItemList expected = new ItemRequestWithItemList
                (1L, "Item_description", null, List.of());
        ItemRequestWithItemList actual = itemRequestService.getRequestById(userId, requestId);

        assertEquals(expected, actual);
    }

    @Test
    void getByIdWithWrongUserIdValidationTest() {
        Long userId = 9999L;
        Long requestId = 1L;
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));

        Mockito.verify(itemRequestRepository, Mockito.never()).findById(Mockito.anyLong());
    }

    @Test
    void getByIdWithWrongRequestIdValidationTest() {
        Long userId = user.getId();
        Long requestId = 999L;
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));
    }

    @Test
    void getAllByRequesterWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequestsByRequester(userId, 0, 10));

        Mockito.verify(itemRequestRepository, Mockito.never()).findByRequesterId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void getAllWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequests(userId, 0, 10));

        Mockito.verify(itemRequestRepository, Mockito.never()).findByRequesterId(Mockito.anyLong(), Mockito.any());
    }

}