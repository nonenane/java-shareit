package ru.practicum.shareit.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super("Вещь не найдена");
    }
}
