package asia.ptyin.sqloj.course;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public class CourseNotFoundEntity extends EntityNotFoundException
{
    public CourseNotFoundEntity(String courseName)
    {
        super("Course with name '%s' not found.".formatted(courseName));
    }
    public CourseNotFoundEntity(UUID uuid)
    {
        super("Course with UUID '%s' not found.".formatted(uuid));
    }
}
