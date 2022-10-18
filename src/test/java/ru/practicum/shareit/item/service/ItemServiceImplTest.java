package ru.practicum.shareit.item.service;


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
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User user1;
    private User user2;
    private Comment comment;

    @BeforeEach
    public void initEach() {
        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        item = new Item(1L,
                user2.getId(),
                "item",
                "item desc",
                true,

                null);

        comment = new Comment(1L, "text", item, user1, LocalDateTime.now());
    }


    @Test
    void createItem() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));

        Assertions.assertThrows(RuntimeException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });
    }

    @Test
    void patchItem() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            itemService.update(1L, null, ItemMapper.toDTO(item));
        });

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            itemService.update(1L, 1L, ItemMapper.toDTO(item));
        });
    }

    @Test
    void getItemWithOwnerCheck() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findAllByItem_Id(Mockito.anyLong())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_IdOrderByStartDesc(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertEquals(itemService.getItemWithOwnerCheck(1L, 2L).get().getId(), 1L);
    }

    @Test
    void getMyItems() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findPageByOwner_Id(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item));
        when(commentRepository.findAllByItem_Id(Mockito.anyLong())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_IdOrderByStartDesc(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertEquals(itemService.getAll(2L, 0, 100).stream().collect(Collectors.toList()).get(0).getId(), 1L);
    }

    @Test
    void searchItems() {
        Assertions.assertTrue(itemService.search("", 0, 100).isEmpty());
        when(itemRepository.searchItemsPageContainsTextAvailableTrue(Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(List.of(item));
        Assertions.assertEquals(itemService.search("qqq", 0, 100).stream().collect(Collectors.toList()).get(0).getId(), 1L);
    }

    @Test
    void createComment() {
        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.createComment(comment, 1L, 1L);
        });
    }
}