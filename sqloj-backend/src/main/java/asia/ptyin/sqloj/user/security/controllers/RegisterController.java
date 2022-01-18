package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.entities.User;
import asia.ptyin.sqloj.user.entities.UserDto;
import asia.ptyin.sqloj.user.security.UserDetailsAdapter;
import asia.ptyin.sqloj.user.security.exceptions.DuplicateUsernameException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequestMapping("/register")
@RestController
public class RegisterController
{
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public Map<String, Object> register(@Valid @RequestBody UserDto userDto, Authentication authentication)
    {
        var result = new HashMap<String, Object>();
        if(!repository.existsByUsername(userDto.getUsername()))  // if not exists
        {
            var creator = ((UserDetailsAdapter) authentication.getPrincipal()).getUser();
            var encodedPassword = passwordEncoder.encode(userDto.getPassword());
            var newUser = User.registerUser(userDto.getUsername(), encodedPassword, creator);
            repository.save(newUser);
            log.info("Create user '%s' with password '%s' by '%s'".formatted(userDto.getUsername(), userDto.getPassword(), creator.getUsername()));
        }
        else
            throw new DuplicateUsernameException();

        result.put("success", true);
        return result;
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
