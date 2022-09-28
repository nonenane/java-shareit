package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDTO {
    private final Long id;
    private final String name;
    @Email
    private final String email;
}
