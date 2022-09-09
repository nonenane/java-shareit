package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    ItemStorage itemStorage;
    UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Optional<Item> create(Item item) {
        if (item.getName() == null || item.getName().isBlank() ||
                item.getDescription() == null || item.getDescription().isBlank() ||
                item.getAvailable() == null ||
                item.getOwnerId() == null
        )
            throw new ValidationException("Ошибка валидации");

        userStorage.get(item.getOwnerId()).orElseThrow(() -> new NotFoundException(item.getOwnerId().toString()));
        return itemStorage.create(item);
    }

    @Override
    public Optional<Item> update(Item item) {
        Item oldItem = itemStorage.get(item.getId()).orElseThrow(() -> new ItemNotFoundException());

        if (oldItem.getOwnerId() != item.getOwnerId()) {
            throw new NotFoundException("Не хозяин вещи");
        }

        if (item.getName() != null && !item.getName().isBlank())
            oldItem.setName(item.getName());

        if (item.getDescription() != null && !item.getDescription().isBlank())
            oldItem.setDescription(item.getDescription());

        if (item.getAvailable() != null)
            oldItem.setAvailable(item.getAvailable());

        return itemStorage.update(oldItem);
    }

    @Override
    public Optional<Item> get(Long id) {
        return itemStorage.get(id);
    }

    @Override
    public Collection<Item> search(String text) {
        if (text == null || text.isBlank())
            return new ArrayList<>();

        return itemStorage.getAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(s -> s.getName().toLowerCase().contains(text.toLowerCase()) || s.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Collection<Item> getAll(Long ownerId) {
        return itemStorage.getAll().stream().filter(s -> s.getOwnerId() == ownerId).collect(Collectors.toList());
    }

}
