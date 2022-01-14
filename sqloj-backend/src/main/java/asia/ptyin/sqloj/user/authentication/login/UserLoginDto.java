package asia.ptyin.sqloj.user.authentication.login;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User Data Transfer Object
 */
@SuppressWarnings("ClassCanBeRecord")
@Valid
@Data
public class UserLoginDto
{
    @NotNull
    @Size(min=2, max=10)
    private final String username;

    @NotNull
    @Size(min=6, max=20)
    private final String password;
}
