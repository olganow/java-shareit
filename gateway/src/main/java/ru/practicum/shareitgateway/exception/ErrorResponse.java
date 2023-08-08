package ru.practicum.shareitgateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private final String error;

    private final String description;
}