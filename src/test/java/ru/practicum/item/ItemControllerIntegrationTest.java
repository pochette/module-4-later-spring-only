package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserState;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemControllerIntegrationTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private User user;
    private Item item;
    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setRegistrationDate(Instant.now());
        user.setState(UserState.ACTIVE);
        user.setLastName("Burdak");
        user.setFirstName("Andrew");
        user.setEmail("burdak1994@gmail.com");

        item = new Item();
        item.setUser(user);
        item.setDateResolved((Instant.now()));
        item.setUrl("https://metry.intrumnet.com/crm/tools/#stock");
        item.setTitle("Вход в систему");
        item.setTags(new HashSet<>(List.of("java", "kotlin", "C++", "Scala")));

    }

    @Test
    void shouldAddNewItem() {
        em.persist(user);
        ItemDto dto = ItemMapper.toItemDto(item);

        try {
            mvc.perform(post("/items")
                    .header("X-Later-User-Id", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(item.getTitle()))
                .andExpect(jsonPath("$.url").value(item.getUrl()));
            log.debug("Item added: {}", item);

            assertThat(itemRepository.count()).isEqualTo(1);
            log.debug("Item count in repository: {}", itemRepository.count());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteItem() {
        em.persist(user);
        em.persist(item);
        em.flush();
        em.clear();

        try {
            mvc
                .perform(delete("/items/{itemId}", item.getId())
                    .header("X-Later-User-Id", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(em.find(Item.class, item.getId())).isNull();
        assertThat(em.find(User.class, user.getId())).isNotNull();
        em.flush();
        em.clear();
    }

    @Test
    void shouldReturnListOfItemDto() {
        em.persist(user);
        em.persist(item);
        Item item2 = getNewItem();
        em.persist(item2);

        em.flush();
        em.clear();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("state", String.valueOf(GetItemRequest.State.ALL));
        params.add("contentType", String.valueOf(GetItemRequest.ContentType.ALL));

        params.add("sort", String.valueOf(GetItemRequest.Sort.NEWEST));
        params.add("limit", "10");
        params.addAll("tags", List.of("java", "kotlin", "C++", "Scala"));

        try {
            mvc
                .perform(get("/items")
                    .header("X-Later-User-Id", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .params(params)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result
                        .getResponse()
                        .getContentAsString();
                    ItemDto[] itemDtos = mapper.readValue(json, ItemDto[].class);
                    assertThat(itemDtos.length).isEqualTo(2);
                    assertThat(itemDtos[0].getUserId()).isEqualTo(user.getId());
                    assertThat(itemDtos[1].getUserId()).isEqualTo(user.getId());

                });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Item getNewItem() {
        Item newItem = new Item();
        newItem.setUser(user);
        newItem.setDateResolved((Instant.now()));
        newItem.setUrl("https://metry.intrumnet.com/crm/tools/#stock");
        newItem.setTags(new HashSet<>(Set.of("java", "kotlin", "C++", "Scala")));
        return newItem;
    }

}
