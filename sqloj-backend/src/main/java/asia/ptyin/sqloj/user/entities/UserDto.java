package asia.ptyin.sqloj.user.entities;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("ClassCanBeRecord")
@Data
public class UserDto implements Serializable
{
    private final String username;
    private final String password;

}
