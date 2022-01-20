package asia.ptyin.sqloj.user;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException
{
    public UserNotFoundException(String username)
    {
        super("User with username '%s' not found.".formatted(username));
    }
    public UserNotFoundException(UUID uuid)
    {
        super("User with UUID '%s' not found.".formatted(uuid));
    }
}
