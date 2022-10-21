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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
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
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private Item newItem;
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

        newItem = new Item(1L,
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
        when(itemRepository.save(item)).thenReturn(item);
        final Optional<ItemDto> itemDto = itemService.create(2L, ItemMapper.toDTO(item));

        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto.get(), ItemMapper.toDTO(item));
    }

    @Test
    void createItemNotFoundUser() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });
    }

    @Test
    void createItemValidateException() {
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new ItemRequest(1L, "new", user2, LocalDateTime.now())));
        item.setRequest(new ItemRequest(1L, "new", user2, LocalDateTime.now()));

        item.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });

        item.setName("");
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });
        item.setName("new");

        item.setDescription(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });

        item.setDescription("");
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });
        item.setDescription("new description");

        item.setAvailable(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(2L, ItemMapper.toDTO(item));
        });
        item.setAvailable(true);

        item.setOwnerId(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            itemService.create(null, ItemMapper.toDTO(item));
        });
    }


    @Test
    void patchItem() {
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(new ItemRequest(1L, "new", user2, LocalDateTime.now())));
        item.setRequest(new ItemRequest(1L, "new", user2, LocalDateTime.now()));

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(item)).thenReturn(item);
        final ItemDto itemDto = itemService.update(2L, 1L, ItemMapper.toDTO(item)).get();
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, ItemMapper.toDTO(item));
    }

    @Test
    void patchItemChangeValue() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(item)).thenReturn(newItem);

        item.setName(null);
        item.setDescription(null);
        item.setAvailable(null);
        ItemDto itemDto = itemService.update(2L, 1L, ItemMapper.toDTO(item)).get();
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, ItemMapper.toDTO(newItem));

        item.setName("");
        item.setDescription("");
        item.setAvailable(true);
        itemDto = itemService.update(2L, 1L, ItemMapper.toDTO(item)).get();
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, ItemMapper.toDTO(newItem));

        item.setName("New name");
        item.setName("New description");
        itemDto = itemService.update(2L, 1L, ItemMapper.toDTO(item)).get();
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, ItemMapper.toDTO(newItem));
    }

    @Test
    void patchItemAccessDeniedException() {
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(new ItemRequest(1L, "new", user2, LocalDateTime.now())));
        item.setRequest(new ItemRequest(1L, "new", user2, LocalDateTime.now()));

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            itemService.update(1L, 1L, ItemMapper.toDTO(item));
        });
    }

    @Test
    void patchItemNotFound() {
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        item.setRequest(new ItemRequest(1L, "new", user2, LocalDateTime.now()));

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.update(1L, null, ItemMapper.toDTO(item));
        });

        item.setRequest(null);
        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemService.update(1L, null, ItemMapper.toDTO(item));
        });
    }

    @Test
    void getItem() {
        when(itemRepository.findById(0L)).thenReturn(Optional.ofNullable(item));
        final ItemDto itemDto = itemService.get(0L).get();
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, ItemMapper.toDTO(item));
    }

    @Test
    void getItemNotFound() {
        when(itemRepository.findById(0L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemService.get(0L);
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
    void getItemWithOwnerCheckNotEqual() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findAllByItem_Id(Mockito.anyLong())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_IdOrderByStartDesc(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertEquals(itemService.getItemWithOwnerCheck(1L, 3L).get().getId(), 1L);
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
        Assertions.assertTrue(itemService.search(null, 0, 100).isEmpty());
        when(itemRepository.searchItemsPageContainsTextAvailableTrue(Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(List.of(item));
        Assertions.assertEquals(itemService.search("qqq", 0, 100).stream().collect(Collectors.toList()).get(0).getId(), 1L);
    }

    @Test
    void createComment() {
        List<Booking> bookingCollection = List.of(new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item, user1, BookingStatus.WAITING));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(bookingCollection);

        when(commentRepository.save(comment)).thenReturn(comment);

        final Comment newCommect = itemService.createComment(comment, 1L, 1L);
        Assertions.assertNotNull(newCommect);
        Assertions.assertEquals(newCommect,comment);
    }

    @Test
    void createCommentBadRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.createComment(comment, 1L, 1L);
        });
    }

    @Test
    void createCommentNotFoundItem() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemService.createComment(comment, 1L, 1L);
        });
    }

    @Test
    void createCommentNotFoundUser() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.createComment(comment, 1L, 1L);
        });
    }
}