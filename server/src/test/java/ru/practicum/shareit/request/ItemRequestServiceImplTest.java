package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    ItemRepository itemRepository;
    private User user;
    private User user2;
    private ItemRequestDto addItemRequestDto;
    private ItemRequest itemRequest;
    private ItemRequestShortDto itemRequestShortDto;
    private Item item;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("user@test.testz")
                .build();

        user2 = User.builder()
                .id(2L)
                .name("UserAnoter_Name")
                .email("userAnother@test.testz")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item Name")
                .description("Item description")
                .owner(user)
                .available(true)
                .request(itemRequest)
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .requester(user)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        addItemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .requesterId(itemRequest.getRequester().getId())
                .description("Description")
                .created(itemRequest.getCreated())
                .items(List.of())
                .build();

        itemRequestShortDto = ItemRequestShortDto.builder()
                .id(itemRequest.getId())
                .description("Description")
                .build();
    }

    @Test
    public void createRequestTest() {
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequestDto actualNewRequest = itemRequestService.createRequest(user.getId(), itemRequestShortDto);

        assertNotNull(actualNewRequest);
        assertEquals(addItemRequestDto.getId(), actualNewRequest.getId());
        assertEquals(addItemRequestDto.getDescription(), actualNewRequest.getDescription());
        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void createRequestWithWrongUserIdValidationTest() {
        Long userId = 999L;
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        ItemRequestShortDto dtoShort = new ItemRequestShortDto(1L, "description");
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(userId, dtoShort));

        verify(itemRequestRepository, never()).save(any());
    }


    @Test
    void findByRequesterIdWithWrongUserValidationTest() {
        Long userId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequestsByRequester(userId, 0, 10));

        verify(itemRequestRepository, never()).findByRequesterId(anyLong(), any());
    }


    @Test
    void getRequestByIdWithWrongUserValidationTest() {
        Long userId = 999L;
        Long requestId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));

        verify(itemRequestRepository, never()).findById(anyLong());
    }

    @Test
    void getRequestByIdWithWrongRequestValidationTest() {
        Long userId = user.getId();
        Long requestId = 999L;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(userId, requestId));
    }

    @Test
    void getAllRequestTest() {
        Long userId = user.getId();
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, null);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", userId,
                null, List.of());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterIdNot(anyLong(), any()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> expectedDtoList = List.of(itemRequestDto);
        List<ItemRequestDto> actualDtoList = itemRequestService.getAllRequests(userId, 0, 10);

        assertEquals(expectedDtoList.get(0).getId(), actualDtoList.get(0).getId());
        assertEquals(expectedDtoList.get(0).getDescription(), actualDtoList.get(0).getDescription());
        assertEquals(expectedDtoList.get(0).getItems().size(), actualDtoList.get(0).getItems().size());
    }

    @Test
    void getAllRequestsByRequesterTest() {
        Long userId = user.getId();
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, null);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", userId, null, List.of());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong(), any())).thenReturn(List.of(itemRequest));
        List<ItemRequestDto> expectedDtoList = List.of(itemRequestDto);
        List<ItemRequestDto> actualDtoList = itemRequestService.getAllRequestsByRequester(userId, 0, 10);

        assertEquals(expectedDtoList.get(0).getId(), actualDtoList.get(0).getId());
        assertEquals(expectedDtoList.get(0).getDescription(), actualDtoList.get(0).getDescription());
        assertEquals(expectedDtoList.get(0).getItems().size(), actualDtoList.get(0).getItems().size());
    }

    @Test
    void getRequestByIdTest() {
        Long userId = user.getId();
        Long requestId = 1L;
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, null);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of());

        ItemRequestDto expected = new ItemRequestDto(1L, "description", userId, null, List.of());
        ItemRequestDto actual = itemRequestService.getRequestById(userId, requestId);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getItems(), actual.getItems());
    }


    @Test
    void createWithWrongOwnerWithNotFoundExceptionTest() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(userId, itemRequestShortDto));

        verify(itemRequestRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
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

}
