package asia.ptyin.sqloj.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Log4j2
@RequestMapping("/api")
@RestController
public class AuthController
{
    @PostMapping("/login")   // TODO Change to get method
    String login(@RequestParam String username, @RequestParam String password)
    {
        log.info("User %s is trying to login with password %s".formatted(username, password));
        boolean success = Objects.equals(username, "test") && Objects.equals(password, "test@123");
        log.info("User %s login %s.".formatted(username, success ? "successfully" : "unsuccessfully"));
        return "{\"success\": %s, \"data\": \"%s\"}".formatted(success, "student");

    }

}
