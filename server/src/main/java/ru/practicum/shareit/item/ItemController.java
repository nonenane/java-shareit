package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

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
    public Collection<ItemDtoForOwner> getAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                              @RequestParam(required = false, defaultValue = "100") Integer size) {
        return itemService.getAll(ownerId, from, size);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto).orElseThrow(ItemNotFoundException::new);
    }

    @GetMapping("{itemId}")
    public ItemDtoForOwner get(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId) {
        // return itemService.get(itemId).orElseThrow(ItemNotFoundException::new);

        return itemService.getItemWithOwnerCheck(itemId, ownerId).orElseThrow(ItemNotFoundException::new);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto).orElseThrow(ItemNotFoundException::new);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam(required = false) String text,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "100") Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long id) {

        Comment comment = itemService.createComment(CommentMapper.toComment(commentDto, null, null),
                itemId, id);
        return CommentMapper.toCommentDto(comment);
    }

}
