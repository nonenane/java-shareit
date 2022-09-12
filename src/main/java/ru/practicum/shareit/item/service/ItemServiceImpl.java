package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {


    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public Optional<ItemDto> create(Long ownerId, ItemCreateDto itemCreateDto) {
        log.info("Create Item OwnerID:{}; Item:{}", ownerId, itemCreateDto);
        Item item = itemMapper.toItem(itemCreateDto, ownerId);
        if (item.getName() == null || item.getName().isBlank() ||
                item.getDescription() == null || item.getDescription().isBlank() ||
                item.getAvailable() == null ||
                item.getOwnerId() == null
        )
            throw new ValidationException("Ошибка валидации");

        userStorage.get(item.getOwnerId()).orElseThrow(() -> new NotFoundException(item.getOwnerId().toString()));
        return Optional.ofNullable(itemMapper.toDTO(itemStorage.create(item).orElseThrow(ItemNotFoundException::new)));
    }

    @Override
    public Optional<ItemDto> update(Long ownerId, Long itemId, ItemCreateDto itemCreateDto) {
        log.info("Update Item OwnerID:{}; ItemId:{}; Item:{}", ownerId, itemId, itemCreateDto);

        Item item = itemMapper.toItem(itemCreateDto, ownerId, itemId);

        Item oldItem = itemStorage.get(item.getId()).orElseThrow(ItemNotFoundException::new);

        if (!oldItem.getOwnerId().equals(item.getOwnerId())) {
            throw new NotFoundException("Не хозяин вещи");
        }

        if (item.getName() != null && !item.getName().isBlank())
            oldItem.setName(item.getName());

        if (item.getDescription() != null && !item.getDescription().isBlank())
            oldItem.setDescription(item.getDescription());

        if (item.getAvailable() != null)
            oldItem.setAvailable(item.getAvailable());

        return Optional.of(itemMapper.toDTO(itemStorage.update(oldItem).orElseThrow(ItemNotFoundException::new)));
    }

    @Override
    public Optional<ItemDto> get(Long id) {
        log.info("Get Item ItemId:{}", id);
        return Optional.of(itemMapper.toDTO(itemStorage.get(id).orElseThrow(ItemNotFoundException::new)));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Search Item filter:{}", text);
        if (text == null || text.isBlank())
            return new ArrayList<>();

        return itemStorage.getAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(s -> s.getName().toLowerCase().contains(text.toLowerCase()) || s.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Collection<ItemDto> getAll(Long ownerId) {
        log.info("GET all item ownerID:{}", ownerId);
        return itemStorage.getAll().stream().filter(s -> s.getOwnerId().equals(ownerId)).map(itemMapper::toDTO).collect(Collectors.toList());
    }

}
