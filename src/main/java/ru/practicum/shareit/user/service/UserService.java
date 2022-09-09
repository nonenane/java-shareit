package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> create(User user);
    Optional<User> update(Long id, User user);
    Optional<User> get(Long id);
    void delete(Long id);
    Collection<User> getAll();
}
