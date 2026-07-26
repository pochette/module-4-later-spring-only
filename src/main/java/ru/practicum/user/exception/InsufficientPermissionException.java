package ru.practicum.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.item.common.LaterApplicationException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientPermissionException extends LaterApplicationException {
    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
