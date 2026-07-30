package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UrlMetadataRetriever urlMetadataRetriever;
    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User user;

    @Test
    void pathTagsTestNotFoundException() {
        ModifyItemRequest request = createModifyRequest();
        request.setItemId(999L);

        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            itemService.patchTags(user.getId(), request));
    }

    @Test
    void shouldAddTagsWhenReplaceTagsIsFalse() {
        ModifyItemRequest request = createModifyRequest();
        request
            .getTags()
            .addAll(Set.of("new Tag1", "new Tag2"));
        request.setReplaceTags(false);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        when(itemRepository.save(item)).thenReturn(item);

        ItemDto result = itemService.patchTags(user.getId(), request);

        assertThat(result.getTags()).contains("new Tag1", "new Tag2");
    }

    @Test
    void pathTagsTestInsufficientPermissionException() {
        ModifyItemRequest request = createModifyRequest();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(InsufficientPermissionException.class, () ->
            itemService.patchTags(999L, request));


    }

    private ModifyItemRequest createModifyRequest() {
        return ModifyItemRequest
            .builder()
            .itemId(item.getId())
            .replaceTags(true)
            .tags(new HashSet<>(Set.of("java-update", "kotlin-update")))
            .read(true)
            .build();
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("burdak1994@gmail.com");
        user.setFirstName("Andrew");
        user.setLastName("Burdak");
        user.setState(UserState.ACTIVE);
        user.setRegistrationDate(Instant.now());

        item = new Item();
        item.setUser(user);
        item.setId(10L);
        item.setDateResolved(Instant.now());
        item.setUrl("https://www.google.com/");
        item.setTags(new HashSet<>(Set.of("java", "kotlin", "C#", "programming languages")));
        item.setTitle("Title of article about programming languages");

    }

    @Test
    void shouldDeleteItem() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        itemService.deleteItem(user.getId(), item.getId());

        verify(itemRepository).delete(item);

    }

    @Test
    void shouldPatchTags() {

        ModifyItemRequest request = createModifyRequest();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto result = itemService.patchTags(user.getId(), request);
        assertEquals(result.getTags(), request.getTags());

    }

    @Test
    void shouldReturnEmptyList() {
        GetItemRequest request = createDefaultRequest();
        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class))).thenReturn(Page.empty());
        List<ItemDto> result = itemService.getItems(request);

        assertEquals(Collections.EMPTY_LIST, result);
    }

    @NotNull
    private GetItemRequest createDefaultRequest() {
        GetItemRequest request = new GetItemRequest();

        request.setContentType(GetItemRequest.ContentType.ARTICLE);
        request.setUserId(user.getId());
        request.setState(GetItemRequest.State.READ);
        request.setSort(GetItemRequest.Sort.OLDEST);
        request.setLimit(10);
        return request;
    }

    @Test
    void shouldReturnItems() {
        GetItemRequest request = createDefaultRequest();

        Item item2 = createItem(2L, "Second");

        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(item, item2)));

        List<ItemDto> result = itemService.getItems(request);

        assertEquals(2, result.size());
        assertEquals(item.getId(), result
            .getFirst()
            .getId());

        assertEquals(item2.getId(), result
            .getLast()
            .getId());
        verify(itemRepository).findAll(any(BooleanExpression.class), any(Pageable.class));
    }

    private Item createItem(long id, String title) {
        Item item = new Item();
        item.setId(id);
        item.setUser(user);
        item.setTitle(title);
        item.setUrl("https://example.com/" + id);
        item.setDateResolved(Instant.now());
        item.setTags(Set.of("java"));
        return item;
    }

    @Test
    void shouldSortByNewestAndDefault() {
        GetItemRequest request = createDefaultRequest();
        request.setSort(GetItemRequest.Sort.NEWEST);

        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class)
        ))
            .thenReturn(Page.empty());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        List<ItemDto> result = itemService.getItems(request);
        verify(itemRepository).findAll(
            any(BooleanExpression.class),
            captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertThat(pageable.getSort()).isEqualTo(Sort
            .by("dateResolved")
            .descending());

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

    }

    @Test
    void shouldSortByOldest() {
        GetItemRequest request = createDefaultRequest();
        request.setSort(GetItemRequest.Sort.OLDEST);

        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class)
        ))
            .thenReturn(Page.empty());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        List<ItemDto> result = itemService.getItems(request);
        verify(itemRepository).findAll(
            any(BooleanExpression.class),
            captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertThat(pageable.getSort()).isEqualTo(Sort
            .by("dateResolved")
            .ascending());

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

    }

    @Test
    void shouldSortBySite() {
        GetItemRequest request = createDefaultRequest();
        request.setSort(GetItemRequest.Sort.SITE);

        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class)
        ))
            .thenReturn(Page.empty());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        List<ItemDto> result = itemService.getItems(request);
        verify(itemRepository).findAll(
            any(BooleanExpression.class),
            captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertThat(pageable.getSort()).isEqualTo(Sort
            .by("resolvedUrl")
            .ascending());

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void shouldSortByTitle() {
        GetItemRequest request = createDefaultRequest();
        request.setSort(GetItemRequest.Sort.TITLE);

        when(itemRepository.findAll(
            any(BooleanExpression.class),
            any(Pageable.class)
        ))
            .thenReturn(Page.empty());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        List<ItemDto> result = itemService.getItems(request);

        verify(itemRepository).findAll(
            any(BooleanExpression.class),
            captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertThat(pageable.getSort()).isEqualTo(Sort
            .by("title")
            .ascending());
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void shouldThrowInsufficientPermissionException() {
        User userWrong = new User();
        userWrong.setId(2L);

        item.setUser(userWrong);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        assertThrows(InsufficientPermissionException.class, () ->
            itemService.deleteItem(10L, item.getId()));
        verify(itemRepository, never()).delete(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemDoesNotExist() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> itemService.deleteItem(user.getId(), item.getId()));
        verify(itemRepository, never()).delete(any());
    }

}
