package ru.practicum.ewm.exception.ex;

public class EventPublishedException extends RuntimeException {
    public EventPublishedException(final String message) {
        super(message);
    }
}
