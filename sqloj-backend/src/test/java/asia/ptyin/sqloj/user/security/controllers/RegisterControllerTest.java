package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.security.UserDetailsServiceImpl;
import asia.ptyin.sqloj.user.security.SecurityTestBase;
import asia.ptyin.sqloj.user.security.SecurityTestConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import(SecurityTestConfiguration.class)
class RegisterControllerTest extends SecurityTestBase
{
    @Autowired
    UserRepository repository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserDetailsServiceImpl securityService;

    RegisterControllerTest()
    {
        super("register-test", "test@123");
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "securityService")
    @Test
    void register() throws Exception
    {
        UserDto userDto = new UserDto(testUsername, testPassword);

        when(repository.existsByUsername(testUsername)).thenReturn(false);
        when(repository.save(any())).thenReturn(null);
        mockMvc
                .perform(
                        post("/register")
                                .content(mapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}