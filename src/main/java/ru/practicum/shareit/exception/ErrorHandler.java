package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerException(final NotFoundException e) {
        log.debug("Not found error: {}", e.getMessage());
        return new ErrorResponse("Not Found Exception", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerErrorException(Throwable e) {
        log.debug("Server error {}: {},", e.getClass(), e.getMessage());
        return new ErrorResponse("internal server error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .peek(e -> log.debug("Validation error: {}", e.getDefaultMessage()))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        e -> (e.getDefaultMessage() == null) ? "validation error" : e.getDefaultMessage()
                ));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongState(final NotSupportedStateException e) {
        log.debug("Not found error: {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notAvailableException(final NotAvailableException e) {
        log.debug("Not found error: {}", e.getMessage());
        return new ErrorResponse("Available error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse duplicateException(final CloneNotSupportedException e) {
        log.debug("Not found error: {}", e.getMessage());
        return new ErrorResponse("Duplicate error", e.getMessage());
    }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse alreadyExistsException(final RuntimeException e) {
        log.debug("Not found error: {}", e.getMessage());
        return new ErrorResponse("Already exists error", e.getMessage());
    }


}