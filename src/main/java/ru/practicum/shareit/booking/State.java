package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.NotSupportedStateException;

public enum State {
    ALL, //получение списка всех бронирований текущего пользователя.
    CURRENT,//текущие бронирования
    PAST,//завершённые бронирования
    FUTURE,//будущие бронирования
    WAITING,//ожидающие подтверждения бронирования
    REJECTED; //отклонённые бронирования


    public static State validateState(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (RuntimeException e) {
            throw new NotSupportedStateException("Unknown state: " + state);
        }
    }

    }