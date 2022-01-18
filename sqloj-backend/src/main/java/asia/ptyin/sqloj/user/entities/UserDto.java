package asia.ptyin.sqloj.user.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("ClassCanBeRecord")
@Data
public class UserDto implements Serializable
{
    @NotNull
    @Length(min = 2, max = 32)
    private final String username;
    @NotNull
    @Length(min = 8, max = 32)
    private final String password;

}
