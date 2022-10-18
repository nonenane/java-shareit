package ru.practicum.shareit.request.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;

    User requestor1 = new User(1L, "user1", "user1@email.ru");
    User requestor2 = new User(2L, "user2", "user2@email.ru");
    ItemRequest itemRequest1 = new ItemRequest(1L, "desc1", requestor1, LocalDateTime.now());
    ItemRequest itemRequest2 = new ItemRequest(2L, "desc2", requestor2, LocalDateTime.now());

    @BeforeEach
    public void beforeEach() {
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
    }

    @Test
    void findAllByRequestor_IdOrderByCreatedDesc() {
        Assertions.assertEquals(1,
                itemRequestRepository.findAllByPetitioner_IdOrderByCreatedDesc(1L).size());
    }

    @Test
    void findPageNotMyRequests() {
        Assertions.assertEquals(1,
                itemRequestRepository.findPageNotMyRequests(1L, 0, 5).size());
    }

    @Test
    void findAllNotMyRequests() {
        Assertions.assertEquals(1,
                itemRequestRepository.findAllNotMyRequests(1L).size());
    }
}