package ru.practicum.item;

import java.time.Instant;

public interface UrlMetadataRetriever {

    UrlMetadata retrieve(String url);

    interface UrlMetadata {

        Instant getDateResolver();

        String getMimeType();

        String getNormalUrl();

        String getResolvedUrl();

        String getTitle();

        boolean isHasImage();

        boolean isHasVideo();

    }
}
