package ru.practicum.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ItemMapper {
    public static Item toEntity(ItemDto itemDto, Long userId) {
        Item newItem = new Item();
        newItem.setUserId(userId);
        newItem.setUrl(itemDto.url());
        newItem.setTags(itemDto.tags());
        return newItem;

    }

    public static List<ItemDto> toItemsListDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(ItemMapper.toItemDto(item));
        }
        return result;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getUser.getId(), item.getUrl(), item.getTags());
    }
}
