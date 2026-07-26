package ru.practicum.item.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.practicum.item.dto.ItemInfoWithUrlState;
import ru.practicum.item.ItemShort;
import ru.practicum.item.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemInfoWithUrlState checkUrlStateIsAvailable(ItemShort item) {
        return itemRepository.findById(item.getId()).stream()
                .map(Item::getUrl)
                .map(this::checkServer)
                .map(check -> new ItemInfoWithUrlState(item, check))
                .findFirst().orElse(new ItemInfoWithUrlState(item, false));

    }

    private boolean checkServer(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.optionsForAllow(url).contains(HttpMethod.GET);
        } catch (Exception e) {
            return false;
        }
    }


}
