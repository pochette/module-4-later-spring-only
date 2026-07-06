package ru.practicum.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    ItemDto addNewItem(Long userId, ItemDto item);

    void deleteItem(Long userId, Long itemId);

    List<ItemDto> getItems(Long userId);

}
