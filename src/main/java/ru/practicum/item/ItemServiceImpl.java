package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        Item item = itemRepository.save(ItemDtoMapper.toEntity(itemDto));
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        return ItemDtoMapper.toItemsListDto(itemRepository.findByUserId(userId));
    }

}
