package asia.ptyin.sqloj.user.service;

import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.DuplicateUsernameException;

public interface UserService
{
    boolean registerUser(UserDto userDto, UserEntity creator) throws DuplicateUsernameException;
}
