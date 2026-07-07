package ru.practicum.user;

public record UserDto(Long id,
                      String email,
                      String firstName,
                      String lastName,
                      String registrationDate,
                      UserState state) {
}
