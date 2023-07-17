package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingDto addBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                 @Valid @RequestBody BookingShortDto bookingShortDto) {
        return bookingService.createBooking(id, bookingShortDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingByState(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByState(id, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnersBookingByState(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnersBookingByState(id, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

}
