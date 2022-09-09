package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Optional<Item> create(Item item);
    Optional<Item> get(Long id);
    Optional<Item> update (Item item);
    void delete (Long id);
    Collection<Item> getAll();
}
