package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public UserDTO create(@RequestBody UserDTO userDTO) {
        Optional<UserDTO> user = userService.create(userDTO);
        return user.orElseThrow(() -> new NotFoundException(""));
    }

    @PatchMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<UserDTO> user = userService.update(id, userDTO);
        return user.orElseThrow(() -> new NotFoundException(""));
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return userService.get(id).orElseThrow(() -> new NotFoundException(id.toString()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping
    public Collection<UserDTO> getAll() {
        return userService.getAll();
    }
}
