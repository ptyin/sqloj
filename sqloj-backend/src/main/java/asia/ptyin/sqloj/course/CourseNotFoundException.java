package asia.ptyin.sqloj.course;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public class CourseNotFoundException extends EntityNotFoundException
{
    public CourseNotFoundException(String courseName)
    {
        super("Course with name '%s' not found.".formatted(courseName));
    }
    public CourseNotFoundException(UUID uuid)
    {
        super("Course with UUID '%s' not found.".formatted(uuid));
    }
}
