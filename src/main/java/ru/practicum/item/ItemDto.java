package ru.practicum.item;

import java.util.Set;

public record ItemDto(Long id, Long userId, String url, Set<String> tags) {
}
