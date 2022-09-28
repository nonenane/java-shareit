package ru.practicum.shareit.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super("Редактировать описание вещи может только ее владелец.");
    }
}
