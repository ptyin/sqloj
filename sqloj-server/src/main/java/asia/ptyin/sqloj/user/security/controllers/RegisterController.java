package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.security.service.AuthenticationService;
import asia.ptyin.sqloj.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private UserService userService;
    private AuthenticationService authenticationService;

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public Map<String, Object> register(@Valid @RequestBody UserDto userDto, Authentication authentication)
    {
        var result = new HashMap<String, Object>();
        var creator = authenticationService.getUser(authentication);
        userService.registerUser(userDto, creator);
        log.info("Create user '%s' with password '%s' by '%s'".formatted(userDto.getUsername(), userDto.getPassword(), creator.getUsername()));

        result.put("success", true);
        return result;
    }

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }
}
