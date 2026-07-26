package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.common.NotFoundException;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.QItem;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.exception.InsufficientPermissionException;
import ru.practicum.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private UrlMetadataRetriever urlMetadataRetriever;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Пользователь вещи не" +
                " найден"));
        UrlMetadataRetriever.UrlMetadata urlMetadata = urlMetadataRetriever.retrieve(itemDto.getUrl());

        Item item = itemRepository.save(ItemMapper.toEntity(itemDto, user));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(GetItemRequest request) {
        QItem item = QItem.item;

        List<BooleanExpression> condition = new ArrayList<>();

        condition.add(item.user.id.eq(request.getUserId()));

        GetItemRequest.State state = request.getState();

        if (!state.equals(GetItemRequest.State.ALL)) {
            condition.add(makeConditionState(state));
        }
        GetItemRequest.ContentType contentType = request.getContentType();
        if (!contentType.equals(GetItemRequest.ContentType.ALL)) {
            condition.add(makeContentType(contentType));
        }
        if (request.hasTags()) {
            condition.add(item.tags
                .any()
                .in(request.getTags()));
        }
        BooleanExpression finalCondition = condition
            .stream()
            .reduce(BooleanExpression::and)
            .get();
        Sort sort = makeOrderByClause(request.getSort());
        PageRequest pageRequest = PageRequest.of(0, request.getLimit(), sort);

        Iterable<Item> result = itemRepository.findAll(finalCondition, pageRequest);
        return ItemMapper.toItemsListDto(result);

    }

    @Override
    public ItemDto patchTags(Long userId, ModifyItemRequest request) {
        Item item = getAndCheckPermissionByUser(userId, request.getItemId());

        item.setUnread(!request.isRead());
        if (request.isReplaceTags()) {
            item
                .getTags()
                .clear();
            item.setTags(request.getTags());
        } else {
            item
                .getTags()
                .addAll(request.getTags());
        }
        Item resultItem = itemRepository.save(item);
        return ItemMapper.toItemDto(resultItem);
    }

    private Item getAndCheckPermissionByUser(Long userId, Long itemId) {
        Item item =
            itemRepository
                .findById(itemId)
                .orElseThrow(() ->
                    new NotFoundException("Item by id " + itemId + " and by user " +
                        "id " + userId + " was not found"));

        if (!item
            .getUser()
            .getId()
            .equals(userId)) {

            throw new InsufficientPermissionException("You do not have permission to perform this operation");
        }
        return item;
    }

    /// /
//public enum Sort { NEWEST, OLDEST, TITLE }
//    public enum ContentType { ALL, ARTICLE, IMAGE, VIDEO }
//    public enum State { ALL, UNREAD, READ }
    private BooleanExpression makeConditionState(GetItemRequest.State state) {

        if (state.equals(GetItemRequest.State.READ)) {
            return QItem.item.unread.isFalse();
        } else {
            return QItem.item.unread.isTrue();
        }
    }

    private BooleanExpression makeContentType(GetItemRequest.ContentType contentType) {
        if (contentType.equals(GetItemRequest.ContentType.ARTICLE)) {
            return QItem.item.mimeType.eq("text");
        } else if (contentType.equals(GetItemRequest.ContentType.IMAGE)) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(GetItemRequest.Sort sort) {
        switch (sort) {
            case TITLE -> {
                return Sort
                    .by("title")
                    .ascending();
            }

            case SITE -> {
                return Sort
                    .by("resolvedUrl")
                    .ascending();
            }

            case OLDEST -> {
                return Sort
                    .by("dateResolved")
                    .ascending();
            }

            default -> {
                return Sort
                    .by("dateResolved")
                    .descending();
            }
        }
    }

}
