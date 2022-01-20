package asia.ptyin.sqloj.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;


@ResponseStatus(HttpStatus.NOT_FOUND)
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
