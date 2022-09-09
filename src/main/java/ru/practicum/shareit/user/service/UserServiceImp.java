package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    UserStorage userStorage;

    @Autowired
    public UserServiceImp(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> create(User user) {

        if (user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) throw new ValidationException("Ошибка валидации");

        if (isDuplicateEmail(user.getEmail())) throw new DuplicateEmailException();

        return userStorage.create(user);
    }

    public Optional<User> update(Long id, User user) {

        User oldUser = userStorage.get(id).orElseThrow(() -> new NotFoundException(id.toString()));
        if (user.getName() != null && !user.getName().isBlank())
            oldUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (isDuplicateEmail(user.getEmail())) throw new DuplicateEmailException();
            oldUser.setEmail(user.getEmail());
        }

        return userStorage.update(oldUser);
    }

    public Optional<User> get(Long id) {
        return userStorage.get(id);
    }

    public void delete(Long id) {
        userStorage.delete(id);
        if (userStorage.get(id).isPresent()) {
            throw new ValidationException("Пользователь не удален");
        }
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    private boolean isDuplicateEmail(String email) {
        return userStorage.getAll().stream().map(User::getEmail).anyMatch(s -> s.contains(email));
    }

}
