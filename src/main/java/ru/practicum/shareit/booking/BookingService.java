package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long id, BookingEntryDto bookingDto);

    BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long bookingId);

    List<BookingDto> getAllBookingByState(Long id, String state);

    BookingDto approveBooking(Long id, Long bookingId, Boolean approved);

    List<BookingDto> getAllOwnersBookingByState(Long id, String state);
}
