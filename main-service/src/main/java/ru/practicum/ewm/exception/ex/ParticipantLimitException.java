package ru.practicum.ewm.exception.ex;

public class ParticipantLimitException extends RuntimeException {
    public ParticipantLimitException(final String message) {
        super(message);
    }
}
