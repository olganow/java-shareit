package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotSupportedStateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.bookingToBookingDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private static final Sort SORT_BY_DESC = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public BookingDto createBooking(Long id, BookingEntryDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Item with id= " + id + " hasn't not found"));

        if (bookingDto.getStart().isEqual(bookingDto.getEnd()) ||
                bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new NotAvailableException("The end day can't be earlier than the start day");
        }
        if (id.equals(item.getOwner().getId())) {
            throw new NotFoundException("Your own item with id= " + id + " can't book");
        }
        if (!item.getAvailable()) {
            throw new NotAvailableException("Item with id= " + id + " isn't not available");
        }

        Booking booking = Booking
                .builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();

        booking.setBooker(userRepository.findById(id).orElseThrow(()
                -> new NotFoundException("User with id= " + id + " hasn't not found")));

        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return bookingToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long id, Long bookingId) {
        validateUser(id);
        Booking booking = validateBooking(bookingId);
        if (!booking.getBooker().getId().equals(id) && !booking.getItem().getOwner().getId().equals(id)) {
            throw new NotFoundException("User with id= " + id + " is not booker or owner");
        }
        return bookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingByState(Long id, String stateString) {
        validateUser(id);
        List<Booking> bookingList;
        LocalDateTime time = LocalDateTime.now();

        State state = validateState(stateString);
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(id, SORT_BY_DESC);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(id, time, time, SORT_BY_DESC);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndBefore(id, time, SORT_BY_DESC);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfter(id, time, SORT_BY_DESC);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(id, Status.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(id, Status.REJECTED, SORT_BY_DESC);
                break;
            default:
                bookingList = Collections.emptyList();
        }
        return bookingList.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    public List<BookingDto> getAllOwnersBookingByState(Long id, String stateString) {
        validateUser(id);
        List<Booking> bookingList;
        LocalDateTime now = LocalDateTime.now();

        State state = validateState(stateString);
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerId(id, SORT_BY_DESC);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerIdAndEndBefore(id, now, SORT_BY_DESC);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(id, now, now, SORT_BY_DESC);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartAfter(id, now, SORT_BY_DESC);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(id, Status.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(id, Status.REJECTED, SORT_BY_DESC);
                break;
            default:
                bookingList = Collections.emptyList();
        }

        return bookingList.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto approveBooking(Long id, Long bookingId, Boolean approved) {
        validateUser(id);
        Booking booking = validateBooking(bookingId);

        if (!booking.getItem().getOwner().getId().equals(id)) {
            throw new NotFoundException("User with id= " + id + " is not owner");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new NotAvailableException("Item with id= " + bookingId + " has been approved yet");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingToBookingDto(bookingRepository.save(booking));
    }

    private State validateState(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (Exception e) {
            throw new NotSupportedStateException("Unknown state: " + state);
        }
    }

    private Booking validateBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Booking with id= " + bookingId + " hasn't not found"));
    }

    private void validateUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id= " + id + " hasn't not found");
        }
    }
}