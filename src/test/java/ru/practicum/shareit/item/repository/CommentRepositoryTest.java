package ru.practicum.shareit.item.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;
    User user1 = new User(1L,
            "qwe",
            "werw@email.ru");
    User user2 = new User(2L,
            "qwe",
            "wer@email.ru");
    Item item = new Item(1L,
            user2.getId(),
            "name",
            "desc",
            true,
            null);
    Comment comment = new Comment(1L,
            "text",
            item,
            user1,
            LocalDateTime.now());

    @Test
    void findAllByItem_Id() {
        userRepository.save(user1);
        userRepository.save(user2);
        System.out.println(commentRepository.save(comment));
        Assertions.assertEquals(1, commentRepository.findAllByItem_Id(1L).size());
    }
}