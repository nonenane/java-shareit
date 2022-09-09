package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserServiceImp;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemMapper itemMapper;
    ItemService itemService;

    @Autowired
    public ItemController(ItemMapper itemMapper, ItemService itemService, UserServiceImp userServiceImp) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.getAll(id).stream().map(itemMapper::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long id, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        Optional<Item> item = itemService.create(itemMapper.toItem(itemCreateDto, id));
        item.orElseThrow(() -> new ItemNotFoundException());
        return itemMapper.toDTO(item.get());
    }

    @GetMapping("{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long itemId) {
        return itemMapper.toDTO(itemService.get(itemId).orElseThrow(() -> new ItemNotFoundException()));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long itemId, @Valid @RequestBody ItemCreateDto itemDto) {
        Optional<Item> item = itemService.update(itemMapper.toItem(itemDto, id, itemId));
        return itemMapper.toDTO(item.get());
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long id, @RequestParam(required = false) String text) {

        return itemService.search(text).stream().map(itemMapper::toDTO).collect(Collectors.toList());
    }
}
