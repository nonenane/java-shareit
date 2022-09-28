package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> create(UserDTO userDTO);

    Optional<UserDTO> update(Long id, UserDTO userDTO);

    Optional<UserDTO> get(Long id);

    void delete(Long id);

    Collection<UserDTO> getAll();
}