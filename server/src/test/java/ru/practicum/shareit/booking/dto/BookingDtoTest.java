package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    private static final String TIME_PATTERN_TEST = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN_TEST);

    @Test
    void bookingCreationDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingCreationDto = BookingDto.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();
        JsonContent<BookingDto> result = json.write(bookingCreationDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo((now.plusDays(1)).format(formatter));
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo((now.plusDays(2)).format(formatter));
    }


}
