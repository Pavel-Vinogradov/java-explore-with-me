package ru.practicum.ewm.exception.ex;

public class RequestNotFountException extends RuntimeException {
    public RequestNotFountException(final String message) {
        super(message);
    }
}
