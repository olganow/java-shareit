package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
public class BookingShortDto {

    private Long id;

    @FutureOrPresent
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent
    private LocalDateTime end;

    private Long itemId;
}
