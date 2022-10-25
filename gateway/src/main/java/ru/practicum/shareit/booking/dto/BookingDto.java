package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.annotation.StartBeforeEnd;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@StartBeforeEnd
public class BookingDto {
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    private ItemDto item;
    private UserDTO booker;
    private BookingStatus status;
    private Long itemId;

    private Long bookerId;
}