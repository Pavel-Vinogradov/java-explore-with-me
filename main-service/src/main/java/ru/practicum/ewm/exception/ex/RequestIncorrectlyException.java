package ru.practicum.ewm.exception.ex;

public class RequestIncorrectlyException extends RuntimeException {
    public RequestIncorrectlyException(final String message) {
        super(message);
    }
}
