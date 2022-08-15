package net.ptyin.sqloj.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)  // 409
public class DuplicateUsernameException extends RuntimeException
{
    public DuplicateUsernameException(String username)
    {
        super("Username '%s' already exists.".formatted(username));
    }

    public DuplicateUsernameException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
