package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        userDto = new UserDto(
            1L,
            "john.doe@mail.com",
            "John",
            "Doe",
            "2022.07.03 19:55:00",
            LocalDate.of(2000, 1, 1)

            , UserState.ACTIVE);
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(any()))
            .thenReturn(userDto);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
            .andExpect(jsonPath("$.firstName", is(userDto.firstName())))
            .andExpect(jsonPath("$.lastName", is(userDto.lastName())))
            .andExpect(jsonPath("$.email", is(userDto.email())));
    }
}