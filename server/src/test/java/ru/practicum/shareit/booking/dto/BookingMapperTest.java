package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.practicum.shareit.booking.dto.BookingMapper.bookingToBookingShortDto;
import static ru.practicum.shareit.booking.dto.BookingMapper.bookingToItemBookingDto;


class BookingMapperTest {
    private Booking booking;
    private User user;
    private Item item;
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "James", "James.Bond@test.tttt");

        item = new Item(1L, "Car", "Aston Martin", true, user, null);

        booking = new Booking(1L, start, end, item, user, Status.WAITING);
    }


    @Test
    void bookingToBookingShortDtoTest() {
        BookingShortDto createBookingDto = new BookingShortDto(user.getId(), start, end, item.getId());
        Booking booking = bookingToBookingShortDto(createBookingDto);

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
    }

    @Test
    void bookingToItemBookingDtoTest() {
        BookingItemDto bookingItemDto = bookingToItemBookingDto(booking);

        assertThat(bookingItemDto.getId()).isEqualTo(1L);
        assertThat(bookingItemDto.getStart()).isEqualTo(start);
        assertThat(bookingItemDto.getEnd()).isEqualTo(end);
        assertThat(bookingItemDto.getItemId()).isEqualTo(item.getId());
        assertThat(bookingItemDto.getBookerId()).isEqualTo(1L);
    }

    @Test
    void bookingToBookingDtoTest() {
        BookingDto bookingDto = BookingMapper.bookingToBookingDto(booking);
        BookingDto.Item itemActual = new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName());
        BookingDto.Booker bookerActual = new BookingDto.Booker(booking.getBooker().getId(), booking.getBooker().getName());

        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getStart()).isEqualTo(booking.getStart());
        assertThat(bookingDto.getEnd()).isEqualTo(booking.getEnd());
        assertThat(bookingDto.getStatus()).isEqualTo(booking.getStatus());
        assertThat(bookingDto.getItem()).isEqualTo(itemActual);
        assertThat(bookingDto.getBooker()).isEqualTo(bookerActual);
    }
}
