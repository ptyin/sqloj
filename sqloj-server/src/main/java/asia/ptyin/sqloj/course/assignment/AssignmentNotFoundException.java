package asia.ptyin.sqloj.course.assignment;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public class AssignmentNotFoundException extends EntityNotFoundException
{
    public AssignmentNotFoundException(UUID uuid)
    {
        super("Assignment with UUID '%s' not found.".formatted(uuid));
    }
}
