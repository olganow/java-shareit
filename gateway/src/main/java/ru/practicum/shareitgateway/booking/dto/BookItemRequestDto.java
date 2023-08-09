package ru.practicum.shareitgateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareitgateway.util.valid.DateValidator;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@DateValidator
public class BookItemRequestDto {

    private long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}