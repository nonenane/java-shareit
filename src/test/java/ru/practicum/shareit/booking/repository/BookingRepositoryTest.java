package ru.practicum.shareit.booking.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    User user = new User(1L, "user", "user@email.ru");
    User booker = new User(2L, "booker", "booker@email.ru");
    Item item = new Item(1L,
            user.getId(),
            "name",
            "desc",
            true,
            null);
    Booking booking = new Booking(1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            item,
            booker,
            BookingStatus.WAITING);

    Long bookerId;


    @BeforeEach
    public void beforeEach() {
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookerId = bookingRepository.save(booking).getBooker().getId();
    }

    @Test
    void findAllByBooker_IdOrderByStartDesc() {
        Assertions.assertEquals(1,
                bookingRepository.findAllByItem_IdOrderByStartDesc(1L).size());
    }

    @Test
    void findAllByStatusAndBooker_IdOrderByStartDesc() {
        Assertions.assertEquals(0,
                bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.APPROVED, bookerId)
                        .size());
        Assertions.assertEquals(1,
                bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.WAITING, bookerId)
                        .size());
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc() {
        Assertions.assertEquals(1,
                bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().minusDays(1))
                        .size());
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc() {
        Assertions.assertEquals(1,
                bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(bookerId,
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(1))
                        .size());
    }

    @Test
    void findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc() {
        Assertions.assertEquals(1,
                bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(bookerId,
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().minusDays(1))
                        .size());
    }
}