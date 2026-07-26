package ru.practicum.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ItemMapper {
    public static Item toEntity(ItemDto itemDto, User user) {
        Item newItem = new Item();
        newItem.setUser(user);
        newItem.setUrl(itemDto.getUrl());
        newItem.setTags(itemDto.getTags());
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
        return new ItemDto(item.getId(), item
            .getUser()
            .getId(), item.getUrl(), item.getResolvedUrl(),
            item.getTitle(),
            item.isHasImage(),
            item.isHasVideo()
            , item.isUnread(),
            item
                .getDateResolved()
                .toString(),
            new HashSet<>(item.getTags()));
    }

}
