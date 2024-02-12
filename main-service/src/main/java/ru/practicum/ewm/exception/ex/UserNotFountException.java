package ru.practicum.ewm.exception.ex;

public class UserNotFountException extends RuntimeException {
    public UserNotFountException(final String message) {
        super(message);
    }
}
