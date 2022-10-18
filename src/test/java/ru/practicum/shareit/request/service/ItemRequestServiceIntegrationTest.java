package ru.practicum.shareit.request.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceIntegrationTest {

    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    UserService userService;

    UserDTO requestor = new UserDTO(null, "requestor", "requestor@email.ru");
    UserDTO user = new UserDTO(null, "user", "user@email.ru");
    ItemRequest itemRequest = new ItemRequest(null, "desc", null, LocalDateTime.now());
    ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

    @Test
    void createItemRequest() {
        userService.create(requestor);
        ItemRequestDto savedItemRequestDto = itemRequestService.createItemRequest(itemRequestDto, 1L).get();
        Assertions.assertEquals(1L, savedItemRequestDto.getId());
        Assertions.assertEquals("desc", savedItemRequestDto.getDescription());
    }

    @Test
    void getMyItemRequests() {
        userService.create(requestor);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        List<ItemRequestDto> savedItemsRequestDto = itemRequestService.getMyItemRequests(1L);
        Assertions.assertEquals(1, savedItemsRequestDto.size());
        Assertions.assertEquals(1L, savedItemsRequestDto.get(0).getId());
        Assertions.assertEquals("desc", savedItemsRequestDto.get(0).getDescription());
    }

    @Test
    void getItemRequests() {
        userService.create(requestor);
        userService.create(user);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        List<ItemRequestDto> savedItemsRequestDto = itemRequestService.getItemRequests(0,
                100,
                2L);
        Assertions.assertEquals(1, savedItemsRequestDto.size());
        Assertions.assertEquals(1L, savedItemsRequestDto.get(0).getId());
        Assertions.assertEquals("desc", savedItemsRequestDto.get(0).getDescription());
    }

    @Test
    void getItemRequest() {
        userService.create(requestor);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDto savedItemRequestDto = itemRequestService.getItemRequest(1L, 1L).get();
        Assertions.assertEquals(1L, savedItemRequestDto.getId());
        Assertions.assertEquals("desc", savedItemRequestDto.getDescription());
    }
}