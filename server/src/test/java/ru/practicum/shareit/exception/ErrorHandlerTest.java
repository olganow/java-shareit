package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @BeforeEach
    public void beforeEach() {
        initMocks(this);
    }

    @Test
    public void alreadyExistsExceptionTest() {
        AlreadyExistsException exception = new AlreadyExistsException("Already exists error");
        ErrorResponse response = errorHandler.alreadyExistsException(exception);
        assertEquals("Already exists error", response.getError());
    }

    @Test
    public void duplicateExceptionTest() {

        CloneNotSupportedException exception = new CloneNotSupportedException("Duplicate error");
        ErrorResponse response = errorHandler.duplicateException(exception);
        assertEquals("Duplicate error", response.getError());
    }

    @Test
    public void handlerExceptionTest() {
        NotFoundException exception = new NotFoundException("Not Found Exception");
        ErrorResponse response = errorHandler.handlerException(exception);
        assertEquals("Not Found Exception", response.getError());
    }

    @Test
    public void handleServerErrorExceptionTest() {
        NotFoundException exception = new NotFoundException("internal server error");
        ErrorResponse response = errorHandler.handleServerErrorException(exception);
        assertEquals("internal server error", response.getError());
    }

    @Test
    public void handleWrongStateTest() {

        NotSupportedStateException exception = new NotSupportedStateException("internal server error");
        ErrorResponse response = errorHandler.handleWrongState(exception);
        assertEquals("internal server error", response.getError());
    }

    @Test
    public void notAvailableExceptionTest() {
        NotAvailableException exception = new NotAvailableException("Available error");
        ErrorResponse response = errorHandler.notAvailableException(exception);
        assertEquals("Available error", response.getError());
    }

}
