package ru.practicum.shareit.exception;

public class NotSupportedStateException extends RuntimeException {
    public NotSupportedStateException(String message) {
        super(message);
    }
}