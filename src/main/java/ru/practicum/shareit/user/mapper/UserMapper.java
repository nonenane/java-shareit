package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserDTO userDTO) {
        return new User(null, userDTO.getName(), userDTO.getEmail());
    }

    public UserDTO userDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
