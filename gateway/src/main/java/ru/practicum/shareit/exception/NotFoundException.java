package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    private final String objectType;

    public NotFoundException(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectType() {
        return objectType;
    }
}