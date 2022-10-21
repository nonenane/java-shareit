package ru.practicum.shareit.user.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
public class UserMapper {

    public static User toUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail());
    }

    public static UserDTO userDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
