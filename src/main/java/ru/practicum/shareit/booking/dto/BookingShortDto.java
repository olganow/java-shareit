package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constants.TIME_PATTERN;

@Data
@Builder
public class BookingShortDto {

    private Long id;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern = TIME_PATTERN)
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @JsonFormat(pattern = TIME_PATTERN)
    @FutureOrPresent
    private LocalDateTime end;

    private Long itemId;
}
