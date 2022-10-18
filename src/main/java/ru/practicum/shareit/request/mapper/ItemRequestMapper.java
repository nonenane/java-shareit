package ru.practicum.shareit.request.mapper;


import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null)
            return null;

        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null);
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User petitioner) {
        if (itemRequestDto == null)
            return null;

        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                petitioner,
                itemRequestDto.getCreated());
    }
}