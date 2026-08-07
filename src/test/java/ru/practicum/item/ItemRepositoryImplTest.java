package ru.practicum.item;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.item.dto.ItemInfoWithUrlState;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.item.repository.ItemRepositoryImpl;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


//TODO Подготовить интеграционные тесты для ItemRepository.
// Тесты: shouldSaveItem(),
// shouldDeleteItemFromDatabase(),
// shouldReplaceTags(),
// shouldAppendTags(),
// shouldReturnUserItems(),
// shouldFilterByReadState(),
// shouldFilterByContentType(),
// shouldSortItemsByNewest(),
// shouldSortItemsByOldest(),
// shouldThrowWhenItemNotFound(), shouldThrowWhenUserIsNotOwner().
//



//
//
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ItemRepositoryImplTest {
    @InjectMocks
    private ItemRepositoryImpl itemRepositoryImpl;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlMetadataRetriever urlMetadataRetriever;

    @Mock
    private ItemShort itemShort;

    @Mock
    private Item item;



    @Test
    void checkUrlStateIsAvailable() {

        try (MockWebServer server = new MockWebServer()) {

            server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Allow", "GET"));

            server.start();
            String url = server
                .url("/")
                .toString();

            when(itemShort.getId()).thenReturn(1L);
            when(itemShort.getUrl()).thenReturn(url);

            when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

            when(item.getUrl()).thenReturn(url);

            ItemInfoWithUrlState result =
                itemRepositoryImpl.checkUrlStateIsAvailable(itemShort);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(url, result.getUrl());
            assertTrue(result.isState());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    void checkUrlStateIsAvailableTest() {

        try (MockWebServer server = new MockWebServer()) {


            server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Allow", "GET"));

            server.start();
            String url = server
                .url("/")
                .toString();

            when(itemShort.getId()).thenReturn(1L);

            when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

            when(item.getUrl())
                .thenReturn(url);

            ItemInfoWithUrlState result =
                itemRepositoryImpl.checkUrlStateIsAvailable(itemShort);

            assertNotNull(result);
            assertEquals(1L, result.getId());

            assertTrue(result.isState());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}