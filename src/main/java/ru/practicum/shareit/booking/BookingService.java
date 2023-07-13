package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long id, BookingShortDto bookingShortDto);

    BookingDto getBookingById(Long id, Long bookingId);

    List<BookingDto> getAllBookingByState(Long id, String state);

    BookingDto approveBooking(Long id, Long bookingId, Boolean approved);

    List<BookingDto> getAllOwnersBookingByState(Long id, String state);
}
