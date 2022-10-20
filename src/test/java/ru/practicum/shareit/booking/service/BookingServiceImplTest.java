package ru.practicum.shareit.booking.service;


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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        when(userRepository.findById(22L)).thenReturn(Optional.ofNullable(user1));

        Assertions.assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking, 22L, 22L);
        });

        booking.setId(null);
        item.setAvailable(false);
        Assertions.assertThrows(ItemNotAvailableException.class, () -> {
            bookingService.createBooking(booking, 22L, 22L);
        });

        booking.setEnd(LocalDateTime.now().minusDays(1));
        Assertions.assertThrows(ValidationException.class, () -> {
            bookingService.createBooking(booking, 22L, 22L);
        });

        booking.setEnd(LocalDateTime.now());
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

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.save(booking)).thenReturn(booking);
        final BookingDto findBooking =  bookingService.confirmBooking(1L, true, 2L).get();
        Assertions.assertNotNull(findBooking);

        booking.setStatus(BookingStatus.WAITING);
        BookingDto findBookingRejected =  bookingService.confirmBooking(1L, false, 2L).get();
        Assertions.assertNotNull(findBookingRejected);
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
    void getAllMyBookingsNotFound() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());


        Assertions.assertThrows(NotFoundException.class
                , () -> {
                    bookingService.getAllMyBookings(1L,
                            BookingState.PAST,
                            0,
                            100);
                });

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        Assertions.assertThrows(RuntimeException.class
                , () -> {
                    bookingService.getAllMyBookings(1L,
                            null,
                            0,
                            100);
                });
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