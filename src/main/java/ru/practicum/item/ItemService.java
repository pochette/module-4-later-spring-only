package ru.practicum.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    Item addNewItem(Long userId, Item item);

    void deleteItem(Long userId, Long itemId);

    List<Item> getItems(Long userId);

}
