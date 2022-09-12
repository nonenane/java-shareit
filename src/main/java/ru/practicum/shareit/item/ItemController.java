package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {


    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getAll(ownerId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemService.create(ownerId, itemCreateDto).orElseThrow(ItemNotFoundException::new);
    }

    @GetMapping("{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId) {
        return itemService.get(itemId).orElseThrow(ItemNotFoundException::new);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId, @Valid @RequestBody ItemCreateDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto).orElseThrow(ItemNotFoundException::new);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestParam(required = false) String text) {
        return itemService.search(text);
    }
}
