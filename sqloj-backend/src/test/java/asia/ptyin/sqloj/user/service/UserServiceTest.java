package asia.ptyin.sqloj.user.service;

import asia.ptyin.sqloj.SqlOjApplication;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.UserNotFoundException;
import asia.ptyin.sqloj.user.security.UserDetailsAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest
{

    @Autowired
    UserService userService;

    @Test
    void findNotExistedUser()
    {
        assertThrows(UserNotFoundException.class, () -> userService.findUser(UUID.randomUUID()));
    }

    @Test
    void deleteNotExistedUser()
    {
        userService.deleteUser(new UserEntity());
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    @Test
    void registerUser()
    {
        UserDto userDto = new UserDto("user-service-test", "user-service-test@123");
        var userDetails = (UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userService.registerUser(userDto, userDetails.getUser());
        assertNotNull(user);
        userService.deleteUser(user);
    }
}