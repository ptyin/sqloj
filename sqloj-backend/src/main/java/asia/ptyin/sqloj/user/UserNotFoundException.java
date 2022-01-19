package asia.ptyin.sqloj.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String username)
    {
        super("User with username '%s' not found.".formatted(username));
    }

    public UserNotFoundException(UUID uuid)
    {
        super("User with UUID '%s' not found.".formatted(uuid));
    }

    public UserNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
