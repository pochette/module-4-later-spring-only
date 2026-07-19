package ru.practicum.item;

public interface ItemRepositoryCustom {
    ItemInfoWithUrlState checkUrlStateIsAvailable(ItemShort item);
}
