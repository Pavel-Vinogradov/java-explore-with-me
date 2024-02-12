package ru.practicum.ewm.exception.ex;

public class EventLimitConfirmedException extends RuntimeException {
    public EventLimitConfirmedException(final String message) {
        super(message);
    }
}
