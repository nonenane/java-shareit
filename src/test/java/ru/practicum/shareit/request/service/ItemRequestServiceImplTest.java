package ru.practicum.shareit.request.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private Item item;
    private User user1;
    private User user2;

    @BeforeEach
    public void initEach() {

        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        itemRequest = new ItemRequest(1L, "itemrequest desc", user1, LocalDateTime.now());
        itemRequestDto = new ItemRequestDto(null, "itemrequest desc", null, null);
        item = new Item(1L,
                user2.getId(),
                "item",
                "item desc",
                true,
                null);
    }

    @Test
    void createItemRequest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            itemRequestService.createItemRequest(ItemRequestMapper.toItemRequestDto(itemRequest), 1L);
        });
    }

    @Test
    void getMyItemRequests() {
        when(userRepository.save(user2)).thenReturn(user2);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRequestRepository.findAllByPetitioner_IdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(Mockito.anyLong())).thenReturn(List.of(item));

        List<ItemRequestDto> list = itemRequestService.getMyItemRequests(2L);
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getId(), 1L);
        Assertions.assertEquals(list.get(0).getItems().get(0).getId(), 1L);
    }

    @Test
    void getItemRequests() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRequestRepository.findPageNotMyRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ArrayList<>());

        Assertions.assertTrue(itemRequestService.getItemRequests(0, 100, 2L).isEmpty());
    }

    @Test
    void getItemRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.findAllByRequest_Id(Mockito.anyLong())).thenReturn(List.of(item));

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(1L, 1L).get();
        Assertions.assertEquals(itemRequestDto.getId(), 1L);
    }
}