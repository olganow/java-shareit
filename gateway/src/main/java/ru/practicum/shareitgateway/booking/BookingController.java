package ru.practicum.shareitgateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.booking.dto.BookItemRequestDto;
import ru.practicum.shareitgateway.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareitgateway.util.Constants.REQUEST_HEADER_USER_ID;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating a booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }


    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                         @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        BookingState bookingState = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect state: " + stateParam));

        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwner(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                           @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10")
                                           Integer size) {

        BookingState bookingState = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect state: " + stateParam));

        log.info("Get booking with status {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingByOwner(userId, bookingState, from, size);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {} of userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveStatus(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam boolean approved) {
        log.info("Approve booking with id =  {}", bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }
}