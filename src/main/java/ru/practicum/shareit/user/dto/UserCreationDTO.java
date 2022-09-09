package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserCreationDTO {
    private String name;
    @Email
    private String email;
}
