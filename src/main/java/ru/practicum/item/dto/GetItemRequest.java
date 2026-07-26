package ru.practicum.item.dto;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
public class GetItemRequest {

    private long userId;
    private State state;
    private ContentType contentType;
    private Sort sort;
    private Integer limit;
    private List<String> tags;

    public static GetItemRequest of(Long userId,
                                    String state,
                                    String contentType,
                                    String sort,
                                    int limit,
                                    List<String> tags
                                    ) {
        GetItemRequest request = new GetItemRequest();
        request.setUserId(userId);
        request.setState(State.valueOf(state.toUpperCase()));
        request.setContentType(ContentType.valueOf(contentType.toUpperCase()));
        request.setSort(Sort.valueOf(sort.toUpperCase()));
        request.setLimit(limit);
        if (tags != null) {
            request.setTags(tags);
        }
        return request;

    }

    public boolean hasTags() {
        return tags != null || !tags.isEmpty();
    }

    public enum Sort { NEWEST, OLDEST, TITLE, SITE }
    public enum ContentType { ALL, ARTICLE, IMAGE, VIDEO }
    public enum State { ALL, UNREAD, READ }
}
