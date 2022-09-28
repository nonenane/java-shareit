package ru.practicum.shareit.exception;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException() {
        super("Бронирование не найдено.");
    }
}
