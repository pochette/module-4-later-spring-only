package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemInfoWithUrlState implements ItemShort {
    private final Long id;
    private final String url;
    private final boolean state;

    public ItemInfoWithUrlState(ItemShort itemShort, boolean state) {
        this.id = itemShort.getId();
        this.url = itemShort.getUrl();
        this.state = state;
    }
}

