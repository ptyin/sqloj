package net.ptyin.sqloj.user.service;

import net.ptyin.sqloj.course.CourseEntity;
import net.ptyin.sqloj.user.UserEntity;
import net.ptyin.sqloj.user.UserDto;
import net.ptyin.sqloj.user.DuplicateUsernameException;
import net.ptyin.sqloj.user.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService
{
    UserEntity registerUser(UserDto userDto, UserEntity creator) throws DuplicateUsernameException;
    UserEntity findUser(UUID uuid) throws UserNotFoundException;
    UserEntity findUser(String username) throws UserNotFoundException;
    List<UserEntity> findAllUser(Iterable<UUID> uuid);
    void deleteUser(UserEntity user);

    List<CourseEntity> getUserParticipatedCourseList(UserEntity user);

}
