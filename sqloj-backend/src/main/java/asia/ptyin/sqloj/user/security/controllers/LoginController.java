package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.user.security.UserDetailsAdapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/***
 * Stub controller, the actual login action is delegated spring security.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@RequestMapping("/login")
@RestController
public class LoginController
{
    @GetMapping
    public String login()
    {
        return "Using post method to login";
    }

    @RequestMapping("/success")
    public Map<String, Object> success()
    {
        var userDetails = (UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userDetails.getUser();
        var result = new HashMap<String, Object>();
        log.info("User %s login successfully with password %s".formatted(user.getUsername(), user.getPassword()));
        result.put("success", true);
        result.put("data", user.getRole());
        return result;
    }

    @RequestMapping("/failure")
    public Map<String, Object> failure(@RequestParam String username, @RequestParam String password)
    {
        var result = new HashMap<String, Object>();
        log.info("User %s login successfully with password %s".formatted(username, password));
        result.put("success", false);
        return result;
    }
}
