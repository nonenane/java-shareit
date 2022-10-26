package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.groupValidate.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ItemDto {
    @Null(groups = Create.class)
    private Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String name;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long requestId;
}
