package asia.ptyin.sqloj.user.service;

import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(UserDto userDto, UserEntity creator)
    {
        if(repository.existsByUsername(userDto.getUsername()))  // if not exists
            throw new DuplicateUsernameException(userDto.getUsername());

        var encodedPassword = passwordEncoder.encode(userDto.getPassword());
        var newUser = UserEntity.createUser(userDto.getUsername(), encodedPassword, creator);
        repository.save(newUser);

        return newUser;
    }

    @Override
    public UserEntity findUser(UUID uuid)
    {
        var user = repository.findById(uuid).orElse(null);
        if(user == null)
            throw new UserNotFoundException(uuid);
        return user;
    }

    @Override
    public UserEntity findUser(String username) throws UserNotFoundException
    {
        var user = repository.findByUsername(username);
        if(user == null)
            throw new UserNotFoundException(username);
        return user;
    }

    @Override
    public List<UserEntity> findAllUser(Iterable<UUID> uuid)
    {
        return repository.findAllById(uuid);
    }

    @Override
    public void deleteUser(UserEntity user)
    {
        repository.delete(user);
    }

    @Override
    public List<CourseEntity> getUserParticipatedCourseList(UserEntity user)
    {

        return null;
    }

    @Autowired
    public void setRepository(UserRepository repository)
    {
        this.repository = repository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
    }
}
