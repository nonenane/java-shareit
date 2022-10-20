package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private User user;
    private User userUpdate;
    private UserDTO userDTO;
    private UserDTO userUpdateDTO;

    @BeforeEach
    public void initEach() {
        user = new User(1L, "user1", "user1@email.ru");
        userUpdate = new User(1L, "userNew", "userNew@email.ru");
        userDTO = UserMapper.userDto(user);
        userUpdateDTO = UserMapper.userDto(userUpdate);
    }

    @Test
    void createUser() {
        user.setName(null);
        userDTO = UserMapper.userDto(user);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.create(userDTO);
        });

        user.setName("");
        userDTO = UserMapper.userDto(user);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.create(userDTO);
        });

        user.setName("user1");
        user.setEmail(null);
        userDTO = UserMapper.userDto(user);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.create(userDTO);
        });

        user.setEmail("");
        userDTO = UserMapper.userDto(user);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.create(userDTO);
        });

        user.setEmail("user1@email.ru");
        userDTO = UserMapper.userDto(user);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        final UserDTO userGet = userService.create(userDTO).get();

        assertNotNull(userGet);
        assertEquals(userDTO, userGet);
    }

    @Test
    void updateNotFoundUser() {
        user.setId(null);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.update(1L, userDTO);
        });
    }

    @Test
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(userUpdate)).thenReturn(userUpdate);
        final UserDTO userGet = userService.update(1L, userUpdateDTO).get();
        assertNotNull(userGet);
        assertEquals(userUpdateDTO, userGet);
    }

    @Test
    void updateUserNullNameAndEmail() {
        userUpdate.setName(null);
        userUpdate.setEmail(null);
        userUpdateDTO = UserMapper.userDto(userUpdate);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        final UserDTO userGet = userService.update(1L, userUpdateDTO).get();
        assertNotNull(userGet);
        assertEquals(userDTO, userGet);
    }

    @Test
    void updateUserBlackNameAndEmail() {
        userUpdate.setName("");
        userUpdate.setEmail("");
        userUpdateDTO = UserMapper.userDto(userUpdate);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        final UserDTO userGet = userService.update(1L, userUpdateDTO).get();
        assertNotNull(userGet);
        assertEquals(userDTO, userGet);
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

    @Test
    void deleteUser() {
        when(userRepository.findById(1l)).thenReturn(Optional.ofNullable(user));
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.delete(1L);
        });
    }

    @Test
    void userNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.get(1L);
        });
    }
}