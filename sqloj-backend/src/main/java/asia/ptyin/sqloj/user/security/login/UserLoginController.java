package asia.ptyin.sqloj.user.security.login;

import asia.ptyin.sqloj.user.UserRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequestMapping("/login")
@RestController
public class UserLoginController
{
    @GetMapping
    public Map<String, String> login()
    {
        var map = new HashMap<String, String>();
        map.put("abc", "def");
        return map;
    }

    @PostMapping
    public Map<String, Object> login(@Valid UserLoginDto user)
    {
        log.info(user.getUsername());
        var result = new HashMap<String, Object>();
        log.info("User %s is trying to login with password %s".formatted(user.getUsername(), user.getPassword()));
        boolean success = Objects.equals(user.getUsername(), "test") && Objects.equals(user.getPassword(), "test@123");
        result.put("success", success);
        result.put("data", UserRole.STUDENT);
        log.info("User %s login %s.".formatted(user.getUsername(), success ? "successfully" : "unsuccessfully"));
        return result;
    }
}
