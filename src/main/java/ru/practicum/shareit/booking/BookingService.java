package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long id, BookingEntryDto bookingDto);

    BookingDto getBookingById(Long id, Long bookingId);

    List<BookingDto> getAllBookingByState(Long id, String state);

    BookingDto approveBooking(Long id, Long bookingId, Boolean approved);

    List<BookingDto> getAllOwnersBookingByState(Long id, String state);
}
