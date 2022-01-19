package asia.ptyin.sqloj.user.service;

import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.DuplicateUsernameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(UserDto userDto, UserEntity creator)
    {
        if(!repository.existsByUsername(userDto.getUsername()))  // if not exists
        {
            var encodedPassword = passwordEncoder.encode(userDto.getPassword());
            var newUser = UserEntity.createUser(userDto.getUsername(), encodedPassword, creator);
            repository.save(newUser);
        }
        else
            throw new DuplicateUsernameException();
        return true;
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
