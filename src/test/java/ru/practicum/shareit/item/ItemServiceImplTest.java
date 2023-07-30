package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
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
        when(itemRequestRepository.save(any())).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(userId, itemRequestShortDto);

        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequestShortDto.getDescription());
    }

    @Test
    void createRequestWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(userId, itemRequestShortDto));

        verify(itemRequestRepository, never()).save(any());
    }


    @Test
    void getByIdTest() {
        Long userId = user.getId();
        Long requestId = 1L;
        ItemRequest itemRequest = new ItemRequest(1L, "Item_description", user, null);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of());
        ItemRequestDto expected = new ItemRequestDto(1L, "Item_description", user, null, List.of());
        ItemRequestDto actual = itemRequestService.getRequestById(userId, requestId);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreated(), actual.getCreated());

    }

    @Test
    void getByIdWithWrongUserIdValidationTest() {
        Long userId = 9999L;
        Long requestId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));

        verify(itemRequestRepository, never()).findById(anyLong());
    }

    @Test
    void getByIdWithWrongRequestIdValidationTest() {
        Long userId = user.getId();
        Long requestId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));
    }

    @Test
    void getAllByRequesterWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequestsByRequester(userId, 0, 10));

        verify(itemRequestRepository, never()).findByRequesterId(anyLong(), any());
    }

    @Test
    void getAllWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequests(userId, 0, 10));

        verify(itemRequestRepository, never()).findByRequesterId(anyLong(), any());
    }

}