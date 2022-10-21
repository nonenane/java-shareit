package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(Long ownerID, ItemDto itemDto);

    Optional<ItemDto> update(Long ownerID, Long itemId, ItemDto itemDto);

    Optional<ItemDto> get(Long id);

    Collection<ItemDto> search(String text, Integer from, Integer size);

    Collection<ItemDtoForOwner> getAll(Long ownerId, Integer from, Integer size);

    Comment createComment(Comment comment, Long itemId, Long createrId);

    Optional<ItemDtoForOwner> getItemWithOwnerCheck(Long itemId, Long ownerId);
}
