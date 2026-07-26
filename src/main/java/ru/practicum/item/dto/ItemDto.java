package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Set;

@Value
@Getter
@Builder(toBuilder = true)
public class ItemDto {
    Long id;
    Long userId;
    String url;

    String resolvedUrl;
    String title;

    boolean hasImage;
    boolean hasVideo;
    boolean unread;
    String dateResolved;
    Set<String> tags;

    public ItemDto(Long id, Long userId, String url, String resolvedUrl, String title, boolean hasImage,
                   boolean hasVideo, boolean unread, String dateResolved, Set<String> tags) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.resolvedUrl = resolvedUrl;
        this.title = title;
        this.hasImage = hasImage;
        this.hasVideo = hasVideo;
        this.unread = unread;
        this.dateResolved = dateResolved;
        this.tags = tags;
    }
}