package asia.ptyin.sqloj.course;

import asia.ptyin.sqloj.user.UserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class CourseDto implements Serializable
{
    @NotNull
    @NotBlank
    private final String name;
    private final String description;


    private final Date startedAt;
    private final Date endedAt;

    // List of participated user uuid.
    private List<UUID> participatorList;
}
