package ru.practicum.user;

import ru.practicum.item.ItemDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static List<UserDto> toDtosList(Iterable<User> userList) {
        List<UserDto> result = new ArrayList<>();
        for (User user : userList) {
            result.add(UserMapper.toDto(user));
        }
        return result;
    }

    public static UserDto toDto(User user) {
        String regDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(user.getRegistrationDate());
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                regDate, user.getState());

    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setState(userDto.state());
        return user;
    }
}
