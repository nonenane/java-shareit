package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserServiceImp;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserServiceImp userServiceImp;

    @Autowired
    public UserController(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }


    @PostMapping
    public UserDTO create(@Valid @RequestBody UserCreationDTO userDTO) {
        Optional<UserDTO> user = userServiceImp.create(userDTO);
        return user.orElseThrow(() -> new NotFoundException(""));
    }

    @PatchMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserCreationDTO userDTO) {
        Optional<UserDTO> user = userServiceImp.update(id, userDTO);
        return user.orElseThrow(() -> new NotFoundException(""));
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return userServiceImp.get(id).orElseThrow(() -> new NotFoundException(id.toString()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userServiceImp.delete(id);
    }

    @GetMapping
    public Collection<UserDTO> getAll() {
        return userServiceImp.getAll();
    }
}
