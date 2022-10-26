package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping()
    public Optional<ItemRequestDto> createItemRequest(@Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto,
                                                      @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("Выполнен запрос createItemRequest");
        return itemRequestClient.createItemRequest(itemRequestDto, id);
    }

    @GetMapping()
    public List<ItemRequestDto> getMyItemRequests(@RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос getMyItemRequests");
        return itemRequestClient.getMyItemRequests(id);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "100") @Positive Integer size,
                                                @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос getItemRequests");
        return itemRequestClient.getItemRequests(from, size, id);
    }


    @GetMapping("/{requestId}")
    public Optional<ItemRequestDto> getItemRequest(@PathVariable Long requestId,
                                                   @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос getItemRequest");
        return itemRequestClient.getItemRequest(requestId, id);
    }
}
