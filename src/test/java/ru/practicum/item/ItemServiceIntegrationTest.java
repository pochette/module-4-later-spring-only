package ru.practicum.item;

//TODO Проверить ручки
//   +-- GET
//   +-- PATCH
//   +-- DELETE

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.InsufficientPermissionException;
import ru.practicum.common.NotFoundException;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test",
    webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private User user;
    private Item item;

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

    }

    @Test
    void shouldAddNewItem() {
        Item newItem = getNewItem();

        item.setHasImage(true);
        item.setTitle("Вход в систему");
        item.setHasVideo(false);
        item.setTags(new HashSet<>(Set.of("java", "kotlin", "c#")));
        item.setResolvedUrl("https://metry.intrumnet.com/login?redirect_to=/crm/tools");
        item.setUrl("https://metry.intrumnet.com/crm/tools/#stock");
        item.setMimeType("text");

        em.persist(user);

        em.flush();
        em.clear();

        ItemDto itemDto = itemService.addNewItem(user.getId(), ItemMapper.toItemDto(item));
        ItemDto newItemDto = itemService.addNewItem(user.getId(), ItemMapper.toItemDto(newItem));

        em.flush();
        em.clear();

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        query.setParameter("id", itemDto.getId());
        Item itemResult = query.getSingleResult();

        assertThat(itemResult.getId()).isNotNull();
        assertThat(itemResult.getUrl()).isEqualTo(item.getUrl());
        assertThat(itemResult.getTitle()).isEqualTo(item.getTitle());
        assertThat(itemResult.getResolvedUrl()).isEqualTo(item.getResolvedUrl());
        assertThat(itemResult.getMimeType()).isEqualTo(item.getMimeType());
        assertThat(itemResult.getTags()).isEqualTo(item.getTags());

    }

    private Item getNewItem() {
        Item newItem = new Item();
        newItem.setUser(user);
        newItem.setDateResolved((Instant.now()));
//        newItem.setHasImage(false);
//        newItem.setTitle("Item Title by items id: " + newItem.getId());
//        newItem.setHasVideo(false);
//        newItem.setTags(Set.of("java" + newItem.getId(), "kotlin" + newItem.getId(), "c#" + newItem.getId()));
//        newItem.setResolvedUrl("https://metry.intrumnet.com/crm/tools/#stock");
        newItem.setUrl("https://metry.intrumnet.com/crm/tools/#stock");
        return newItem;
    }

    @Test
    void shouldDeleteItem() {
        Item newItem = getNewItem();
        em.persist(user);
        em.persist(item);
        em.persist(newItem);

        em.flush();
        em.clear();

        assertThat(itemRepository.count()).isEqualTo(2);
        itemService.deleteItem(user.getId(), item.getId());

        em.flush();
        em.clear();

        assertTrue(itemRepository
            .findById(item.getId())
            .isEmpty());

        assertThat(userRepository.findById(user.getId()))
            .get()
            .isEqualTo(user);
        assertThat(itemRepository.count()).isEqualTo(1);

        em.flush();
        em.clear();

        //Проверка выброса исключений

        assertThrows(NotFoundException.class, () -> itemService.deleteItem(user.getId(), 999L),
            "Ожидается NotFoundException при удалении несуществующего item");
        assertThrows(InsufficientPermissionException.class, () -> itemService.deleteItem(999L, newItem.getId()),
            "Ожидается InsufficientPermissionException при удалении item несуществующим пользователем");

        //Проверка репозитория, что запись не удалилась

        assertTrue(itemRepository
            .findById(newItem.getId())
            .isPresent());

    }

    @Test
    void shouldGetItems() {
        Item newItem = getNewItem();

        em.persist(user);
        em.persist(item);
        em.persist(newItem);

        em.flush();
        em.clear();

        GetItemRequest getItemRequest = createGetItemRequest();
        ItemDto itemDto = itemService.addNewItem(user.getId(), ItemMapper.toItemDto(item));
        ItemDto itemDto2 = itemService.addNewItem(user.getId(), ItemMapper.toItemDto(newItem));

        List<ItemDto> itemList = itemService.getItems(getItemRequest);

        assertThat(itemList).isNotEmpty();
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).containsExactlyInAnyOrder(itemDto, itemDto2);
        assertThat(itemList
            .get(0)
            .getUserId()).isEqualTo(user.getId());
        assertThat(itemList
            .get(1)
            .getUserId()).isEqualTo(user.getId());
        assertThat(itemList
            .getFirst()
            .getTags()).containsExactlyInAnyOrderElementsOf(getItemRequest.getTags());
        assertThat(itemList
            .getLast()
            .getTags()).containsExactlyInAnyOrderElementsOf(getItemRequest.getTags());

    }

    private GetItemRequest createGetItemRequest() {
        GetItemRequest getItemRequest = new GetItemRequest();
        getItemRequest.setSort(GetItemRequest.Sort.SITE);
        getItemRequest.setLimit(10);
        getItemRequest.setUserId(user.getId());
        getItemRequest.setContentType(GetItemRequest.ContentType.ARTICLE);
        getItemRequest.setTags(new ArrayList<>(item.getTags()));
        getItemRequest.setState(GetItemRequest.State.ALL);
        return getItemRequest;
    }

    @Test
    void shouldPatchTags() {
        Item newItem = getNewItem();
        newItem.setTags(new HashSet<>(Set.of("old Tag1 newItem", "old tag2 newItem")));

        item.setTags(new HashSet<>(Set.of("old Tag1 item", "old Tag2 item", "old Tag3 item")));
        em.persist(user);
        em.persist(item);
        em.persist(newItem);
        em.flush();
        em.clear();

        ModifyItemRequest modifyRequest = ModifyItemRequest
            .builder()
            .itemId(item.getId())
            .read(true)
            .replaceTags(true)
            .tags(Set.of("new Tag1", "new Tag2", "new Tag3"))
            .build();
        ItemDto result = itemService.patchTags(user.getId(), modifyRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTags()).containsExactlyInAnyOrderElementsOf(modifyRequest.getTags());
        assertFalse(result.isUnread());
    }

}
