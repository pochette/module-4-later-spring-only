package ru.practicum.user;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDtoMapper {
    public static UserDto toDto(User user) {
        String regDate = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(user.getRegistrationDate());
        return new UserDto(regDate, user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getState());

    }

    public static User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.id())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .registrationDate(Instant.parse(userDto.registrationDate()))
                .state(userDto.state())
                .email(userDto.email())
                .build();
    }

    public static List<UserDto> toDtosList(List<User> userList) {
        return userList.stream()
                .map(UserDtoMapper::toDto)
                .toList();
    }
}
