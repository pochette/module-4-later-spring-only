package ru.practicum.item;

import java.io.Serializable;
import java.util.Set;

public record ItemDto(Long id, Long userId, String url, Set<String> tags) implements Serializable {
}