package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImp(UserMapper userMapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> create(UserDTO userDTO) {
        log.info("Create User {}", userDTO.toString());
        User user = userMapper.toUser(userDTO);
        if (user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Ошибка валидации");
        }
        return Optional.ofNullable(userMapper.userDto(userRepository.save(user)));
    }

    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        log.info("Update user ID:{}; Data:{}", id, userDTO);
        User user = userMapper.toUser(userDTO);

        User oldUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        if (user.getName() != null && !user.getName().isBlank())
            oldUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }

        return Optional.ofNullable(userMapper.userDto(userRepository.save(oldUser)));
    }

    public Optional<UserDTO> get(Long id) {
        log.info("Get info User ID:{}", id);
        return Optional.ofNullable(userMapper.userDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()))));
    }

    public void delete(Long id) {
        log.info("Delete user ID:{}", id);
        userRepository.deleteById(id);
        if (userRepository.findById(id).isPresent()) {
            throw new ValidationException("Пользователь не удален");
        }
    }

    public Collection<UserDTO> getAll() {
        log.info("Get all users");
        return userRepository.findAll().stream().map(userMapper::userDto).collect(Collectors.toList());
    }

}
