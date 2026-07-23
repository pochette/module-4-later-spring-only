package ru.practicum.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.JsonbTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
            1L,
            "john.doe@mail.com",
            "John",
            "Doe",
            "2022.07.03 19:55:00",
            UserState.ACTIVE);

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result)
            .extractingJsonPathNumberValue("$.id")
            .isEqualTo(1);
        assertThat(result)
            .extractingJsonPathStringValue("$.email")
            .isEqualTo("john.doe@mail.com");
        assertThat(result)
            .extractingJsonPathStringValue("$.firstName")
            .isEqualTo("John");
        assertThat(result)
            .extractingJsonPathStringValue("$.lastName")
            .isEqualTo("Doe");
        assertThat(result)
            .extractingJsonPathStringValue("$.registrationDate")
            .isEqualTo("2022.07.03 19:55:00");

    }
}
