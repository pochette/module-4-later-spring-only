package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = repository.findByUserId(userId);
        return ItemMapper.toItemsListDto(userItems);
    }

    @Override
    @Transactional
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item item = repository.save(ItemMapper.toEntity(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }
}
