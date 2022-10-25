package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class UserDTO {
    @Null(groups = Create.class)
    private Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String name;
    @NotNull(groups = Create.class)
    @Email(groups = Create.class)
    private String email;
}
