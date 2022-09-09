package ru.practicum.shareit.item.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
@Primary
public class FakeItemStorage implements ItemStorage {
    HashMap<Long, Item> inMemoryStorage;
    private Long idCounter;

    public FakeItemStorage() {
        this.inMemoryStorage = new HashMap<>();
        idCounter = 1L;
    }

    @Primary

    @Override
    public Optional<Item> create(Item item) {
        Item itemForSave = new Item(idCounter, item.getOwnerId(), item.getName(), item.getDescription(), item.getAvailable());
        inMemoryStorage.put(idCounter++, itemForSave);
        return Optional.of(itemForSave);
    }

    @Override
    public Optional<Item> get(Long id) {
        return Optional.of(inMemoryStorage.get(id));
    }

    @Override
    public Optional<Item> update(Item item) {
        inMemoryStorage.put(item.getId(), item);
        return get(item.getId());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Collection<Item> getAll() {
        return inMemoryStorage.values();
    }
}
