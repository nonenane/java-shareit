package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.groupValidate.Create;
import ru.practicum.shareit.groupValidate.Update;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }


    @PostMapping
    public Optional<UserDTO> create(@Validated(Create.class) @RequestBody UserDTO userDTO) {
        log.info("Выполнен запрос createUser");
        return userClient.create(userDTO);
    }

    @PatchMapping("/{id}")
    public Optional<UserDTO> update(@PathVariable Long id, @Validated(Update.class) @RequestBody UserDTO userDTO) {
        log.info("Выполнен запрос updateUser");
        return userClient.update(id, userDTO);
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> get(@PathVariable Long id) {
        log.info("Выполнен запрос getUser");
        return userClient.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Выполнен запрос deleteUser");
        userClient.delete(id);
    }

    @GetMapping
    public Collection<UserDTO> getAll() {
        log.info("Выполнен запрос getAllUser");
        return userClient.getAll();
    }
}
