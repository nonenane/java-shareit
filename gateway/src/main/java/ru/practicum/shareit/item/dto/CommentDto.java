package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.groupValidate.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @Null(groups = Create.class)
    Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String text;
    String authorName;
    LocalDateTime created;
}