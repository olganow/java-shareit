package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {
    private LocalDateTime now = LocalDateTime.now();
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private User user;
    private User userAnother;
    private User savedUser;
    private User savedUserAnother;
    private Item item;
    private Item savedItem;
    private BookingDto bookingCreationDto;
    private Booking booking;
    private Booking savedBooking;
    private Booking bookingAnother;
    private Booking savedBookingAnother;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("username@test.testz")
                .build();

        savedUser = userRepository.save(user);

        userAnother = User.builder()
                .id(2L)
                .name("UserAnother_Name")
                .email("userAnotherName@test.testz")
                .build();

        savedUserAnother = userRepository.save(userAnother);

        item = Item.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(savedUser)
                .build();

        savedItem = itemRepository.save(item);

        booking = Booking.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .item(savedItem)
                .booker(savedUserAnother)
                .status(Status.APPROVED)
                .build();

        bookingCreationDto = BookingDto.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();

        savedBooking = bookingRepository.save(booking);

        bookingAnother = Booking.builder()
                .id(5L)
                .start(now.minusDays(3))
                .end(now.minusDays(5))
                .item(savedItem)
                .booker(savedUserAnother)
                .status(Status.APPROVED)
                .build();

        savedBookingAnother = bookingRepository.save(bookingAnother);
    }

    @Test
    public void getAllOwnersBookingByStateTest() {
        long userId = savedUser.getId();
        int from = 0;
        int size = 10;
        List<BookingDto> bookingDtoList =
                bookingService.getAllOwnersBookingByState(userId, String.valueOf(State.ALL), from, size);

        assertEquals(2, bookingDtoList.size());
        assertEquals(savedBooking.getId(), bookingDtoList.get(0).getId());
        assertEquals(savedBookingAnother.getId(), bookingDtoList.get(1).getId());
        assertEquals(savedBooking.getStart(), bookingDtoList.get(0).getStart());
        assertEquals(savedBooking.getEnd(), bookingDtoList.get(0).getEnd());
        assertEquals(savedBookingAnother.getStart(), bookingDtoList.get(1).getStart());
        assertEquals(savedBookingAnother.getEnd(), bookingDtoList.get(1).getEnd());
    }
}
