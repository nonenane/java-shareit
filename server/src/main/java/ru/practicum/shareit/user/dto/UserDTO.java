package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDTO {
    private final Long id;
    private final String name;
    private final String email;
}
