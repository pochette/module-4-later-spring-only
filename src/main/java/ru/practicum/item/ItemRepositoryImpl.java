package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items;

    @Override
    public List<Item> findByUserId(long userId) {
        return items.get(userId);
    }

    @Override
    public Item save(Item item) {
        item.setId(getId());
        items.put(item.getUserId(), Collections.singletonList(item));
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        List<Item> itemsForDelete = items.get(userId);
        items.remove(userId, itemsForDelete);
    }

    private long getId() {
        return items.values().stream()
                .flatMap(List::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0L) + 1;
    }
}
