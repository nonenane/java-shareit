package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;


public interface ItemRequestService {

    Optional<ItemRequestDto> createItemRequest(ItemRequestDto itemRequestDto, Long creatorId);

    List<ItemRequestDto> getMyItemRequests(Long petitionerId);

    List<ItemRequestDto> getItemRequests(Integer from, Integer size, Long petitionerId);

    Optional<ItemRequestDto> getItemRequest(Long requestId, Long petitionerId);
}
