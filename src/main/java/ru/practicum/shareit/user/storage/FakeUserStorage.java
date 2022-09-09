package ru.practicum.shareit.user.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
public class FakeUserStorage implements UserStorage {
    final private Map<Long, User> inMemoryStorage;
    private Long idCounter;

    public FakeUserStorage() {
        this.inMemoryStorage = new HashMap<>();
        idCounter = 1L;
    }

    @Override
    public Optional<User> create(User user) {
        User userForSave = new User(idCounter, user.getName(), user.getEmail());
        inMemoryStorage.put(idCounter++, userForSave);
        return Optional.of(userForSave);
    }

    @Override
    public Optional<User> update(User user) {
        inMemoryStorage.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public void delete(Long id) {
        inMemoryStorage.remove(id);
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(inMemoryStorage.get(id));
    }

    @Override
    public Collection<User> getAll() {
        return inMemoryStorage.values();
    }
}
