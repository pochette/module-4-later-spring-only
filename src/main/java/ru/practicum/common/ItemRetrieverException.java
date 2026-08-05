package ru.practicum.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ItemRetrieverException extends RuntimeException {
    public ItemRetrieverException(String message, Exception e) {
        super(message);
    }

    public ItemRetrieverException(String message) {
        super(message);
    }
}
