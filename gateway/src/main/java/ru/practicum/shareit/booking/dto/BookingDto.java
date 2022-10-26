package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.annotation.StartBeforeEnd;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@StartBeforeEnd(groups = {Create.class, Update.class})
public class BookingDto {
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = {Create.class, Update.class})
    @FutureOrPresent(groups = {Create.class, Update.class})
    private LocalDateTime start;
    @NotNull(groups = {Create.class, Update.class})
    @Future(groups = {Create.class, Update.class})
    private LocalDateTime end;
    private ItemDto item;
    private UserDTO booker;
    private BookingStatus status;
    private Long itemId;
    private Long bookerId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    public static class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    public static class UserDTO {
        private Long id;
        private String name;
        private String email;
    }
}