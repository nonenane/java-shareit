package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit_test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    UserDTO user = new UserDTO(null, "user1", "user1@email.ru");
    UserDTO userPatch = new UserDTO(1L, "patch", "user1@email.ru");

    @Test
    void createUser() {
        UserDTO savedUser = userService.create(user).get();
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("user1", savedUser.getName());
        Assertions.assertEquals("user1@email.ru", savedUser.getEmail());
    }

    @Test
    void patchUser() {
        userService.create(user);
        UserDTO savedUser = userService.update(1L, userPatch).get();
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("patch", savedUser.getName());
    }

    @Test
    void getUser() {
        userService.create(user);
        UserDTO savedUser = userService.get(1L).get();
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("user1", savedUser.getName());
        Assertions.assertEquals("user1@email.ru", savedUser.getEmail());
    }

    @Test
    void getAllUsers() {
        userService.create(user);
        List<UserDTO> savedUsers = userService.getAll().stream().collect(Collectors.toList());
        Assertions.assertEquals(1, savedUsers.size());
        Assertions.assertEquals(1L, savedUsers.get(0).getId());
        Assertions.assertEquals("user1", savedUsers.get(0).getName());
        Assertions.assertEquals("user1@email.ru", savedUsers.get(0).getEmail());
    }

    @Test
    void deleteUser() {
        userService.create(user);
        userService.delete(1L);
        List<UserDTO> savedUsers = userService.getAll().stream().collect(Collectors.toList());
        Assertions.assertEquals(0, savedUsers.size());
    }
}