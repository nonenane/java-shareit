package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void initEach() {
        user = new User(1L, "user1", "user1@email.ru");
        userDTO = UserMapper.userDto(user);
    }

    @Test
    void createUser() {
        user.setName(null);
        userDTO = UserMapper.userDto(user);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.create(userDTO);
        });


        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userRepository.save(user);

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void patchUser() {
        user.setId(null);
        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.update(1L, userDTO);
        });

    }

    @Test
    void getUser() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        userService.get(1L);
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(userService.getAll().isEmpty());
    }
}