package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImp;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserMapper userMapper;
    UserServiceImp userServiceImp;

    @Autowired
    public UserController(UserMapper userMapper, UserServiceImp userServiceImp) {
        this.userMapper = userMapper;
        this.userServiceImp = userServiceImp;
    }


    @PostMapping
    public UserDTO create(@Valid @RequestBody UserCreationDTO userDTO) {
        Optional<User> user = userServiceImp.create(userMapper.toUser(userDTO));
        user.orElseThrow(() -> new NotFoundException(""));
        return userMapper.toDTO(user.get());
    }

    @PatchMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserCreationDTO userDTO) {
        Optional<User> user = userServiceImp.update(id, userMapper.toUser(userDTO));
        return userMapper.toDTO(user.get());
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return userMapper.toDTO(userServiceImp.get(id).orElseThrow(() -> new NotFoundException(id.toString())));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userServiceImp.delete(id);
    }

    @GetMapping
    public Collection<UserDTO> getAll() {
        return userServiceImp.getAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }
}
