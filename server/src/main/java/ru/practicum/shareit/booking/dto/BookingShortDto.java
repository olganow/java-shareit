package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingShortDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
