package ru.practicum.ewm.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.ex.*;
import ru.practicum.ewm.exception.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({DateTimeParseException.class, MethodArgumentNotValidException.class,
            ConstraintViolationException.class, HttpMessageNotReadableException.class,
            LocalDataTimeParseException.class, DateTimeValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRequestIncorrectlyException(final Exception e) {
        log.debug("Произошла ошибка {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler({UserNotFountException.class, CategoryNotFountException.class,
            EventNotFountException.class, RequestNotFountException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFountException(final Exception e) {
        log.debug("Произошла ошибка {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler({EventIncorectException.class, EventPublishedException.class,
            InitiatorEventException.class, RepeatedRequestException.class, ParticipantLimitException.class,
            UpdateStatusRequestEventException.class, EventLimitConfirmedException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIncorectException(final Exception e) {
        log.debug("Произошла ошибка {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();
    }
}
