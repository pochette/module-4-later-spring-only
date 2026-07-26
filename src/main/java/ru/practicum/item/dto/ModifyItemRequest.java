package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Data
@Getter
@Builder(toBuilder = true)

public class ModifyItemRequest {
    private Long itemId;
    private boolean read;
    private boolean replaceTags;
    private Set<String> tags;
}
