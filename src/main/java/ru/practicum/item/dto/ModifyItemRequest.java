package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Getter
@Builder(toBuilder = true)

public class ModifyItemRequest {
    Long itemId;
    boolean read;
    boolean replaceTags;
    Set<String> tags;
}
