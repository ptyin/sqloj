package net.ptyin.sqloj.user.security.controllers;

import net.ptyin.sqloj.user.security.UserDetailsAdapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/***
 * Stub controller, the actual login behavior is delegated to spring security.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@RequestMapping("/login")
@RestController
public class LoginController
{

    @RequestMapping("/success")
    public Map<String, Object> success()
    {
        var userDetails = (UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userDetails.getUser();
        var result = new HashMap<String, Object>();
        log.info("User '%s' login successfully with password '%s'".formatted(user.getUsername(), user.getPassword()));
        result.put("success", true);
        result.put("data", user.getRole());
        return result;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/failure")
    public Map<String, Object> failure(@RequestParam String username, @RequestParam String password)
    {
        var result = new HashMap<String, Object>();
        log.info("Login failed with username '%s' and password '%s'".formatted(username, password));
        result.put("success", false);
        return result;
    }
}
