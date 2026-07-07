package ru.practicum.item;

import java.util.List;

public class ItemDtoMapper {
    public static Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.id())
                .url(itemDto.url())
                .userId(itemDto.userId())
                .tags(itemDto.tags())
                .build();

    }

    public static List<ItemDto> toItemsListDto(List<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::toItemDto)
                .toList();
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getUserId(), item.getUrl(), item.getTags());
    }
}
