package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.entities.UserDto;


public class SecurityTestBase
{
    protected final String testUsername;
    protected final String testPassword;
    protected final UserDto testUserDto;


    public SecurityTestBase(String testUsername, String testPassword)
    {
        this.testUsername = testUsername;
        this.testPassword = testPassword;

        this.testUserDto = new UserDto(testUsername, testPassword);

    }
}
