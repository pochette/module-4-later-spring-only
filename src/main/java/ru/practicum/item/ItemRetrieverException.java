package ru.practicum.item;

public class ItemRetrieverException extends RuntimeException {
    public ItemRetrieverException(String message, Exception e) {
        super(message);
    }

    public ItemRetrieverException(String message) {
        super(message);
    }
}
