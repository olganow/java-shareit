package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    BookingServiceImpl bookingServiceImpl;
    private User user;
    private User userWithBooking;
    private Item item;
    private Booking booking;
    private BookingShortDto bookingShortDto;
    private BookingDto bookingDto;
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now().plusDays(2);

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

        booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(userWithBooking)
                .status(Status.WAITING)
                .build();

        bookingShortDto = BookingShortDto.builder()
                .id(1L)
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(null)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    @Test
    public void createBookingTest() {
        BookingShortDto shortDto = new BookingShortDto(userWithBooking.getId(), start, end, item.getId());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(userWithBooking.getId())).thenReturn(Optional.of(userWithBooking));
        when(bookingRepository.save(Mockito.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        BookingDto bookingdto = bookingService.createBooking(userWithBooking.getId(), shortDto);

        assertThat(bookingdto).hasFieldOrProperty("id");
    }

    @Test
    void createBookingsWithFalseAvailableTest() {
        Long userId = userWithBooking.getId();
        Long itemId = item.getId();
        item.setAvailable(false);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(userWithBooking));
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);
        assertThrows(NotAvailableException.class, () -> bookingService.createBooking(userId, shortDto));

        Mockito.verify(bookingRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createBookingReturnItemNotFoundExceptionTest() {
        Long userId = userWithBooking.getId();
        Long itemId = 9999L;
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(userId, shortDto));
    }

    @Test
    void getBookingByIdTest() {
        Long userId = user.getId();
        Long bookingId = booking.getId();
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.getBookingById(userId, bookingId);

        assertEquals(1L, result.getId());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(1L, result.getItem().getId());
        assertEquals("Item_name", result.getItem().getName());
        assertEquals(2L, result.getBooker().getId());
        assertEquals("User_WithBooking_Name", result.getBooker().getName());
    }

    @Test
    void getBookingByIdWithWrongBookingIdTest() {
        Long bookingId = 9999L;
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(user.getId(), bookingId));
    }

    @Test
    void getBookingByIdWithWrongUserIdTest() {
        Long bookingId = 1L;
        Long userId = 9999L;
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(userId, bookingId));
    }

    @Test
    void getAllOwnersBookingByStateIfStateIsAllTest() {
        Long userId = userWithBooking.getId();
        String state = String.valueOf(State.ALL);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllOwnersBookingByStateIfStateIsAllStateIsCurrentTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.CURRENT);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(Mockito.anyLong(), Mockito.any(),
                Mockito.any(), Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllOwnersBookingByStateIfStateIsPastTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.PAST);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndEndBefore(Mockito.anyLong(), Mockito.any(),
                Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllOwnersBookingByStateIfStateIsFutureTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.FUTURE);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartAfter(Mockito.anyLong(), Mockito.any(),
                Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllOwnersBookingByStateIfStateIsWaitingTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.WAITING);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }


    @Test
    void getAllOwnersBookingByStateIfStateIsRejectedTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.REJECTED);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatus(Mockito.anyLong(), Mockito.any(),
                Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllOwnersBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllOwnersBookingByStateIfUserNotFoundExceptionTest() {
        Long userId = 999L;
        String state = String.valueOf(State.ALL);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.getAllOwnersBookingByState(userId, state, 0, 10));
    }

    @Test
    void getAllOwnersBookingByStateWithWrongStatusExceptionTest() {
        Long userId = user.getId();
        String state = "Wrong_State";
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        assertThrows(NotSupportedStateException.class, () -> bookingService.getAllOwnersBookingByState(userId, state, 0, 10));
    }

    @Test
    void getAllBookingByStateIfStateIsAllTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.ALL);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingByStateIfStateIsCurrentTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.CURRENT);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(Mockito.anyLong(), Mockito.any(),
                Mockito.any(), Mockito.any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingByStateIfStateIsPastTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.PAST);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerIdAndEndBefore(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }


    @Test
    void ggetAllBookingByStateIfStateIsFutureTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.FUTURE);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartAfter(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingByStateIfStateIsWaitingTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.WAITING);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingByStateIfStateIsRejectedTest() {
        Long userId = user.getId();
        String state = String.valueOf(State.REJECTED);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByState(userId, state, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingByStateWithWrongStatusTest() {
        Long userId = user.getId();
        String state = "Wrong_State";
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        assertThrows(NotSupportedStateException.class, () -> bookingService.getAllBookingByState(userId, state, 0, 10));
    }

    @Test
    void getAllBookingByStateWithUserNotFoundExceptionTest() {
        Long userId = 999L;
        String state = String.valueOf(State.ALL);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingByState(userId, state, 0, 10));
    }


}