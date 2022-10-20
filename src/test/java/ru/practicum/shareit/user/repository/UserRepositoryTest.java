package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user = new User(null, "user1", "user1@email.ru");

    @Test
    void findByEmail() {
        userRepository.save(user);

        Assertions.assertEquals(userRepository.findByEmail("user1@email.ru").get().getEmail(), user.getEmail());
    }
}