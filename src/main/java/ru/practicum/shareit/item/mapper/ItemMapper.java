package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public Item toItem(ItemCreateDto itemCreateDto, Long ownerId) {
        return new Item(null, ownerId, itemCreateDto.getName(), itemCreateDto.getDescription(), itemCreateDto.getAvailable());
    }

    public Item toItem(ItemCreateDto itemCreateDto, Long ownerId, Long itemId) {
        return new Item(itemId, ownerId, itemCreateDto.getName(), itemCreateDto.getDescription(), itemCreateDto.getAvailable());
    }

    public ItemDto toDTO(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

}
