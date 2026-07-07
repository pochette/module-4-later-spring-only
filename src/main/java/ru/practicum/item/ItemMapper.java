package ru.practicum.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    public static List<ItemDto> toItemsListDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getUserId(), item.getUrl(), item.getTags());
    }
}
