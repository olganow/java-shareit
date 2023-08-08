package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingDto addBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                 @RequestBody BookingShortDto bookingShortDto) {
        return bookingService.createBooking(id, bookingShortDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingByState(id, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") Long id,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllOwnersBookingByState(id, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

}
