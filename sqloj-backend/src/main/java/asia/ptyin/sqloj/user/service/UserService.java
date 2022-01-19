package asia.ptyin.sqloj.user.service;

import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.DuplicateUsernameException;
import asia.ptyin.sqloj.user.UserNotFoundException;

import java.util.UUID;

public interface UserService
{
    UserEntity registerUser(UserDto userDto, UserEntity creator) throws DuplicateUsernameException;
    UserEntity findUser(UUID uuid) throws UserNotFoundException;
    void deleteUser(UserEntity user);
}
