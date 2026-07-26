package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.user.UserDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Later-User-Id") Long userId,
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Later-User-Id") Long userId,
                             @RequestParam(defaultValue = "unread") String state,
                             @RequestParam(defaultValue = "all") String contentType,
                             @RequestParam(defaultValue = "newest") String sort,
                             @RequestParam(defaultValue = "10") int limit,
                             @RequestParam(required = false) Set<String> tags) {
        GetItemRequest request = GetItemRequest.of(userId, state, contentType, sort, limit, tags
            .stream()
            .toList());

        return itemService.getItems(request);
    }

    @GetMapping
    List<ItemDto> getItems(GetItemRequest req) {
        return itemService.getItems(req);

    }

    @PatchMapping
    public ItemDto patchTags(@RequestHeader("X-Later-User-Id") Long userId,
                             @RequestParam(name = "itemId") Long itemId,
                             @RequestParam(required = false) Set<String> tags,
                             @RequestParam(required = false, defaultValue = "false") boolean replaceTags) {
        ModifyItemRequest request = ModifyItemRequest
            .builder()
            .itemId(itemId)
            .replaceTags(replaceTags)
            .tags(tags)
            .build();
        return itemService.patchTags(userId, request);
    }

}
