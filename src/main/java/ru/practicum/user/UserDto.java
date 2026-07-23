package ru.practicum.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UserDto(Long id,
                      String email,
                      String firstName,
                      String lastName,
                      String registrationDate,
                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                      LocalDate dateOfBirth,
                      UserState state) {
    public Long getId() {
        return id;
    }
}
