package ru.practicum.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional

@RequiredArgsConstructor(onConstructor_ = @Autowired)

@SpringBootTest(properties = "db.name=test",
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {
    private static Long id = 0L;

//        for (UserDto user : sourceUsers) {
//            User entity = UserMapper.toEntity(user);
//            em.persist(entity);
//        }
//        em.flush();
//
//        // when
//        List<UserDto> targetUsers = service.getAllUsers();
//
//        // then
//        assertThat(targetUsers, hasSize(sourceUsers.size()));
//        for (UserDto sourceUser : sourceUsers) {
//            assertThat(targetUsers, hasItem(allOf(
//                hasProperty("id", notNullValue()),
//                hasProperty("firstName", equalTo(sourceUser.firstName())),
//                hasProperty("lastName", equalTo(sourceUser.lastName())),
//                hasProperty("email", equalTo(sourceUser.email()))
//            )));
//        }
//    }
    private final EntityManager em;
    private final UserService service;

    private static Long getNextId() {
        return id++;
    }

    @Rollback(value = false)
    @Test
    void getAllUsers() {
        // given
        List<UserDto> sourceUsers = List.of(
            makeUserDto("ivan@email", "Ivan", "Ivanov"),
            makeUserDto("petr@email", "Petr", "Petrov"),
            makeUserDto("vasilii@email", "Vasilii", "Vasiliev")
        );
        for (UserDto sourceUser : sourceUsers) {
            User entity = UserMapper.toEntity(sourceUser);
            em.persist(entity);
        }
        em.flush();

        List<UserDto> targetUsers = service.getAllUsers();

        assertThat(targetUsers, hasSize(sourceUsers.size()));

        for (UserDto sourceUser : sourceUsers) {
            assertThat(
                targetUsers
                    .stream()
                    .anyMatch(target ->
                        target.id() != null
                            && target
                            .email()
                            .equals(sourceUser.email())
                            && target
                            .lastName()
                            .equals(sourceUser.lastName())
                            && target
                            .firstName()
                            .equals(sourceUser.firstName())
                            && target
                            .state()
                            .equals(UserState.ACTIVE)
                            && target.registrationDate() != null), is(true));

        }

    }

    @Test
    void saveUser() {
        // given
        UserDto userDto = makeUserDto("some@email.com", "Пётр", "Иванов");

        // when
        service.saveUser(userDto);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
            .setParameter("email", userDto.email())
            .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getFirstName(), equalTo(userDto.firstName()));
        assertThat(user.getLastName(), equalTo(userDto.lastName()));
        assertThat(user.getEmail(), equalTo(userDto.email()));
        assertThat(user.getState(), equalTo(userDto.state()));
        assertThat(user.getRegistrationDate(), notNullValue());
    }

    private UserDto makeUserDto(String email, String firstName, String lastName) {
        return new UserDto(
            null,
            email,
            firstName,
            lastName,
            LocalDateTime
                .now()
                .toString(),
            LocalDate.of(1994,1,29),
            UserState.ACTIVE);
    }

}