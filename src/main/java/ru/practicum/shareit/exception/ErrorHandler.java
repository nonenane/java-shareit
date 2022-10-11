package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler
    public ResponseEntity<String> handleValidationException(final ValidationException e) {
        log.info(String.format("error: Ошибка валидации, некорректный параметр %s", e.getParameter()));
        return new ResponseEntity<>(
                String.format("Ошибка валидации, некорректный параметр %s", e.getParameter()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {

        String defaultMessage = e.getMessage()
                .split("default message")[1]
                .split("]")[0]
                .substring(2);

        log.info(String.format("error: Ошибка валидации, некорректный параметр %s", defaultMessage));

        return new ResponseEntity<>(
                String.format("Ошибка валидации, некорректный параметр %s", defaultMessage),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(final NotFoundException e) {
        log.info(String.format("error: %s c таким идентификатором не найден.", e.getObjectType()));
        return new ResponseEntity<>(
                String.format("%s c таким идентификатором не найден.", e.getObjectType()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerItemNotFoundException(final ItemNotFoundException e) {
        log.info(String.format("error: %s", e.getMessage()));
        return new ResponseEntity<>(
                String.format("%s", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateEmailException(final DuplicateEmailException e) {
        log.info("error: DuplicateEmailException");
        return new ResponseEntity<>(
                "DuplicateEmailException",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        log.info("error: Произошла непредвиденная ошибка " + e.getClass());
        return new ResponseEntity<>(
                "Произошла непредвиденная ошибка " + e.getClass(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAccessDeniedException(final AccessDeniedException e) {
        log.info("error: AccessDeniedException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBookingNotFoundException(final BookingNotFoundException e) {
        log.info("error: BookingNotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemNotAvailableException(final ItemNotAvailableException e) {
        log.info("error: ItemNotAvailableException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.info("error: MethodArgumentTypeMismatchException");
        if (Objects.requireNonNull(e.getMessage()).contains("ru.practicum.shareit.booking.state.BookingState"))
            return new ResponseEntity<>(Map.of("error", "Unknown state: UNSUPPORTED_STATUS"), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBadRequestException(final BadRequestException e) {
        log.info("error: BadRequestException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}