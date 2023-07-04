package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                 @Valid @RequestBody BookingEntryDto bookingDto) throws ValidationException {
        return bookingService.addBooking(id, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByState(id, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingsByUserID(@RequestHeader("X-Sharer-User-Id") Long id,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnersBookingByState(id, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

}
