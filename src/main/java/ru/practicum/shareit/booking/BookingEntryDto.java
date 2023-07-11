package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.util.Constants.TIME_PATTERN;

@Data
@AllArgsConstructor
public class BookingEntryDto {
    private Long id;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;

    @NotNull
    private Long itemId;
}
