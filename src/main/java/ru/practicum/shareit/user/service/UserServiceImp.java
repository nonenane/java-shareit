package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImp implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImp(UserMapper userMapper, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> create(UserCreationDTO userDTO) {
        log.info("Create User {}", userDTO.toString());
        User user = userMapper.toUser(userDTO);
        if (user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Ошибка валидации");
        }
        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
        return Optional.ofNullable(userMapper.userDto(userStorage.create(user).orElseThrow(() -> new NotFoundException(""))));
    }

    public Optional<UserDTO> update(Long id, UserCreationDTO userDTO) {
        log.info("Update user ID:{}; Data:{}", id, userDTO);
        User user = userMapper.toUser(userDTO);

        User oldUser = userStorage.get(id).orElseThrow(() -> new NotFoundException(id.toString()));
        if (user.getName() != null && !user.getName().isBlank())
            oldUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (isDuplicateEmail(user.getEmail())) throw new DuplicateEmailException();
            oldUser.setEmail(user.getEmail());
        }

        return Optional.ofNullable(userMapper.userDto(userStorage.update(oldUser).orElseThrow(() -> new NotFoundException(id.toString()))));
    }

    public Optional<UserDTO> get(Long id) {
        log.info("Get info User ID:{}", id);
        return Optional.ofNullable(userMapper.userDto(userStorage.get(id).orElseThrow(() -> new NotFoundException(id.toString()))));
    }

    public void delete(Long id) {
        log.info("Delete user ID:{}", id);
        userStorage.delete(id);
        if (userStorage.get(id).isPresent()) {
            throw new ValidationException("Пользователь не удален");
        }
    }

    public Collection<UserDTO> getAll() {
        log.info("Get all users");
        return userStorage.getAll().stream().map(userMapper::userDto).collect(Collectors.toList());
    }

    private boolean isDuplicateEmail(String email) {
        return userStorage.getAll().stream().map(User::getEmail).anyMatch(s -> s.contains(email));
    }

}
