package asia.ptyin.sqloj.auth.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * User Data Transfer Object
 */
@SuppressWarnings("ClassCanBeRecord")
@Data
public class UserDto
{
    @NotNull
    @Length(min=2, max=10)
    private final String username;

    @NotNull
    @Length(min=6, max=20)
    private final String password;

    @NotNull
    @Pattern(regexp = "teacher|student")
    private final String role;
}
