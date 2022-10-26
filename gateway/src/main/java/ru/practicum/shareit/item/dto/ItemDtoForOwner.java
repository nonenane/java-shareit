package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForOwner {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;

    private List<CommentDto> comments;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    public static class Booking {
        private Long id;
        private Long bookerId;
    }
}



