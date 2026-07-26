package ru.practicum.item.repository;

import ru.practicum.item.dto.ItemInfoWithUrlState;
import ru.practicum.item.ItemShort;

public interface ItemRepositoryCustom {
    ItemInfoWithUrlState checkUrlStateIsAvailable(ItemShort item);
}
