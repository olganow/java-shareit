package ru.practicum.shareitgateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerErrorException(Throwable e) {
        log.debug("Server error {}, {}", e.getClass(), e.getMessage());
        return new ErrorResponse("internal server error", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Caught Illegal Argument Exception: {}", e.getMessage());
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNoSuchElementException(NoSuchElementException e) {
        log.error("Caught IllegalArgumentException: {}", e.getMessage());
        return new ErrorResponse("NoSuchElementException", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(MethodArgumentNotValidException e) {
        log.error("Method Argument Not Valid Exception: {}", e.getMessage());
        return new ErrorResponse("MethodArgumentNotValidException", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint Violation Exception: {}", e.getMessage());
        return new ErrorResponse("MethodArgumentNotValidException", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerConstraintViolationException(final ValidationException e) {
        log.error("Validation Exception: {}", e.getMessage());
        return new ErrorResponse("MValidation Exception ", e.getMessage());
    }

}
