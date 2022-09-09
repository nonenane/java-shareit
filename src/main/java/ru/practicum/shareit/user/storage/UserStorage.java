package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
   Optional<User> create(User user);

    Optional<User> update(User user);

    void delete(Long id);

    Optional<User> get(Long id);

    Collection<User> getAll();

}
