package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;

@UtilityClass
public class BookingMapper {
    public static Booking bookingToBookingShortDto(BookingShortDto bookingShortDto) {
        return Booking.builder()
                .id(bookingShortDto.getId())
                .start(bookingShortDto.getStart())
                .end(bookingShortDto.getEnd())
                .build();
    }

    public static BookingItemDto bookingToItemBookingDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public static BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new BookingDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()))
                .status(booking.getStatus())
                .item(new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()))
                .build();
    }
}
