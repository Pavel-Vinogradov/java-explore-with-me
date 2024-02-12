package ru.practicum.ewm.exception.ex;

public class RepeatedRequestException extends RuntimeException {
    public RepeatedRequestException(final String message) {
        super(message);
    }
}
