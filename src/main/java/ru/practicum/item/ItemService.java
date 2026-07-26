package ru.practicum.item;

import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;

import java.util.List;

interface ItemService {
    ItemDto addNewItem(long userId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemDto> getItems(GetItemRequest request);

    ItemDto patchTags(Long userId, ModifyItemRequest request);

}
