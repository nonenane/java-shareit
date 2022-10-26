package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<UserDTO> create(UserDTO userDTO) {
        log.info("Create User {}", userDTO.toString());
        User user = UserMapper.toUser(userDTO);
        if (user.getName() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        if (user.getName().isBlank()) {
            throw new ValidationException("Ошибка валидации");
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        if (user.getEmail().isBlank()) {
            throw new ValidationException("Ошибка валидации");
        }


        return Optional.ofNullable(UserMapper.userDto(userRepository.save(user)));
    }

    @Transactional
    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        log.info("Update user ID:{}; Data:{}", id, userDTO);
        User user = UserMapper.toUser(userDTO);

        User oldUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        if (user.getName() != null) {
            if (!user.getName().isBlank())
                oldUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            if (!user.getEmail().isBlank()) {
                oldUser.setEmail(user.getEmail());
            }
        }

        return Optional.ofNullable(UserMapper.userDto(userRepository.save(oldUser)));
    }

    public Optional<UserDTO> get(Long id) {
        log.info("Get info User ID:{}", id);
        return Optional.ofNullable(UserMapper.userDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()))));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete user ID:{}", id);
        userRepository.deleteById(id);
        if (userRepository.findById(id).isPresent()) {
            throw new ValidationException("Пользователь не удален");
        }
    }

    public Collection<UserDTO> getAll() {
        log.info("Get all users");
        return userRepository.findAll().stream().map(UserMapper::userDto).collect(Collectors.toList());
    }

}
