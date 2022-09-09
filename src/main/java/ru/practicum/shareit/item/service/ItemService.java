package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<Item> create(Item item);

    Optional<Item> update(Item item);

    Optional<Item> get(Long id);

    Collection<Item> search(String text);

    void delete(Long id);

    Collection<Item> getAll(Long ownerId);
}
