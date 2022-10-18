package ru.practicum.shareit.item.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    User user1 = new User(1L, "user1", "user1@email.ru");
    User user2 = new User(2L, "user2", "user2@email.ru");
    ItemRequest itemRequest = new ItemRequest(1L, "desc", user2, LocalDateTime.now());
    Item item1 = new Item(1L,
            user1.getId(),
            "name1",
            "desc1",
            true,

            itemRequest);
    Item item2 = new Item(2L,
            user2.getId(),
            "name2",
            "desc2",
            true,
            null);

    @BeforeEach
    public void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void findAllByOwner_Id() {
        Assertions.assertEquals(1, itemRepository.findAllByOwnerId(1L).size());
    }

    @Test
    void findPageByOwner_Id() {
        Assertions.assertEquals(1, itemRepository.findPageByOwner_Id(1L, 0, 5).size());
    }

    @Test
    void searchItemsContainsTextAvailableTrue() {
        Assertions.assertEquals(2, itemRepository.searchItemsContainsTextAvailableTrue("name").size());
        Assertions.assertEquals(1, itemRepository.searchItemsContainsTextAvailableTrue("name1").size());
    }

    @Test
    void searchItemsPageContainsTextAvailableTrue() {
        Assertions.assertEquals(2,
                itemRepository.searchItemsPageContainsTextAvailableTrue("name", 0, 5).size());
        Assertions.assertEquals(1,
                itemRepository.searchItemsPageContainsTextAvailableTrue("name1", 0, 5).size());
    }

    @Test
    void findAllByRequest_Id() {
        Assertions.assertEquals(1, itemRepository.findAllByRequest_Id(1L).size());
    }
}