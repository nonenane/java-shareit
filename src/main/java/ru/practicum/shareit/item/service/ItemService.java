package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(Long ownerID, ItemCreateDto itemCreateDto);

    Optional<ItemDto> update(Long ownerID, Long itemId, ItemCreateDto itemCreateDto);

    Optional<ItemDto> get(Long id);

    Collection<ItemDto> search(String text);

    void delete(Long id);

    Collection<ItemDto> getAll(Long ownerId);
}
