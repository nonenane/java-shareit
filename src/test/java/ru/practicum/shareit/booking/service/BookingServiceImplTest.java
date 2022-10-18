package ru.practicum.shareit.booking.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private Item item;
    private User user1;
    private User user2;

    @BeforeEach
    public void initEach() {
        booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                BookingStatus.APPROVED);

        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        item = new Item(1L,
                user2.getId(),
                "item",
                "item desc",
                true,
                null);

    }

    @Test
    void createBooking() { //проверить валидацию
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));

        Assertions.assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking, 22L, 22L);
        });

        booking.setId(null);
        item.setAvailable(false);

        Assertions.assertThrows(ItemNotAvailableException.class, () -> {
            bookingService.createBooking(booking, 22L, 22L);
        });

        item.setAvailable(true);

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.createBooking(booking, 22L, 2L);
        });

        bookingService.createBooking(booking, 22L, 22L);

        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
    }

    @Test
    void confirmBooking() {
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            bookingService.confirmBooking(1L, true, 3L);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            bookingService.confirmBooking(1L, true, 2L);
        });
    }

    @Test
    void getBooking() {
        booking.setBooker(user1);
        booking.setItem(item);

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.getBooking(1L, 3L);
        });

    }

    @Test
    void getAllMyBookings() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<Booking>());

        Assertions.assertTrue(bookingService.getAllMyBookings(1L,
                        BookingState.PAST,
                        0,
                        100)
                .isEmpty());
    }

    @Test
    void getAllBookingsForMyItems() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.findUserItemsBookingsIds(Mockito.anyLong())).thenReturn(new ArrayList<Long>());

        Assertions.assertTrue(bookingService.getAllBookingsForMyItems(1L,
                        BookingState.PAST,
                        null,
                        null)
                .isEmpty());
    }
}