package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;
    private LocalDateTime created;
    private List<ItemDtoForItemRequest> items;
}
