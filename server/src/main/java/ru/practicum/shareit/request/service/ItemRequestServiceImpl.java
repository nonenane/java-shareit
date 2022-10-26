package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    @Transactional
    public Optional<ItemRequestDto> createItemRequest(ItemRequestDto itemRequestDto, Long creatorId) {
        if (itemRequestDto.getId() != null)
            throw new RuntimeException(" Неверное значение id.");

        User petitioner = userRepository.findById(creatorId).orElseThrow(() -> new NotFoundException(""));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, petitioner);
        itemRequest.setCreated(LocalDateTime.now());
        return Optional.ofNullable(ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest)));
    }

    @Override
    public List<ItemRequestDto> getMyItemRequests(Long petitionerId) {
        //проверка корректности petitionerId
        userRepository.findById(petitionerId).orElseThrow(() -> new NotFoundException(""));

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByPetitioner_IdOrderByCreatedDesc(petitionerId);


        return toItemRequestDtos(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Integer from, Integer size, Long petitionerId) {
        userRepository.findById(petitionerId).orElseThrow(() -> new NotFoundException(""));

        List<ItemRequest> itemRequests = itemRequestRepository.findPageNotMyRequests(petitionerId, from, size);

        return toItemRequestDtos(itemRequests);
    }

    private List<ItemRequestDto> toItemRequestDtos(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        itemRequestsDto.forEach(x -> {
            x.setItems(itemRepository.findAllByRequest_Id(x.getId()).stream()
                    .map(ItemMapper::toItemDtoForItemRequest)
                    .collect(Collectors.toList()));
        });

        return itemRequestsDto;
    }

    @Override
    public Optional<ItemRequestDto> getItemRequest(Long requestId, Long petitionerId) {
        //проверка корректности petitioner_id
        userRepository.findById(petitionerId).orElseThrow(() -> new NotFoundException(""));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(""));

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequest_Id(itemRequestDto.getId()).stream()
                .map(ItemMapper::toItemDtoForItemRequest)
                .collect(Collectors.toList()));

        return Optional.ofNullable(itemRequestDto);
    }
}
