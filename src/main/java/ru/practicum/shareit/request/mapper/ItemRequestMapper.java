package ru.practicum.shareit.request.mapper;


import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null);
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User petitioner) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                petitioner,
                itemRequestDto.getCreated());
    }
}