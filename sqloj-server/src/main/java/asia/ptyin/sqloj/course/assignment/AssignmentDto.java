package asia.ptyin.sqloj.course.assignment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Data
public class AssignmentDto
{
    @NotNull
    @NotBlank
    private final String name;

    private final Date startedAt;
    private final Date endedAt;

    private final UUID courseUuid;
}
