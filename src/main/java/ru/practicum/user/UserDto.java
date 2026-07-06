package ru.practicum.user;

public record UserDto(String registrationDate, Long id, String firstName, String lastName, String email,
                      UserState state) {
}
