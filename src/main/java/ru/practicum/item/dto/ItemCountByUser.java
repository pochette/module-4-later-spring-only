package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemCountByUser {
    private Long userId;
    private Long count;

}
