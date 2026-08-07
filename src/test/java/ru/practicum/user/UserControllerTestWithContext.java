package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
class UserControllerTestWithContext {
    @Autowired
    private MockMvc mvc;

    private final UserDto userDto =
        new UserDto(1L, "john.doe@mail.com", "John", "Doe", "2022.07.03 19:55:00", LocalDate.of(1994, 1, 29),
            UserState.ACTIVE);
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(userDto)).thenReturn(userDto);

        mvc
            .perform(post("/users")
                .content(objectMapper.writeValueAsString(userDto))
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

//@SpringBootTest(properties = "db.name=test",
//    webEnvironment = SpringBootTest.WebEnvironment.NONE)
/// /@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class UserControllerTestWithContext {
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private final UserService userService;
//
//    private MockMvc mvc;
//
//    private UserDto userDto;
//
//    @Autowired
//    UserControllerTestWithContext(UserService userService) {
//        this.userService = userService;
//    }
//
//    @BeforeEach
//    void setUp(WebApplicationContext wac) {
//        mvc = MockMvcBuilders
//            .webAppContextSetup(wac)
//            .build();
//
//        userDto = new UserDto(
//            1L,
//            "john.doe@mail.com",
//            "John",
//            "Doe",
//            "2022.07.03 19:55:00",
//            UserState.ACTIVE);
//    }
//
//    @Test
//    void saveNewUser() throws Exception {
//        when(userService.saveUser(any()))
//            .thenReturn(userDto);
//
//        mvc
//            .perform(post("/users")
//                .content(mapper.writeValueAsString(userDto))
//                .characterEncoding(StandardCharsets.UTF_8)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//            .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
//            .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
//            .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//}