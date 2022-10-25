package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {


    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }


    @GetMapping
    public Collection<ItemDtoForOwner> getAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {
        return itemClient.getAll(ownerId, from, size);
    }

    @PostMapping
    public Optional<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemClient.create(ownerId, itemDto);
    }

    @GetMapping("{itemId}")
    public Optional<ItemDtoForOwner> get(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId) {
        // return itemService.get(itemId).orElseThrow(ItemNotFoundException::new);

        return itemClient.getItem(itemId, ownerId);
    }

    @PatchMapping("{itemId}")
    public Optional<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId, @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return itemClient.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam(required = false) String text,
                                      @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {
        return itemClient.search(ownerId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public Optional<CommentDto> createComment(@Validated(Create.class) @RequestBody CommentDto commentDto,
                                              @PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemClient.createComment(commentDto, itemId, id);
    }

}
