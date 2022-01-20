package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest
{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    String testUsername;
    String testPassword;
    UserDto testUserDto;

    @PostConstruct
    public void init()
    {

        testUsername = "admin";
        testPassword = "admin@123";
        testUserDto = new UserDto(testUsername, testPassword);

        when(repository.findByUsername("admin")).thenReturn(UserEntity.createDefaultAdmin("admin", passwordEncoder.encode("admin@123")));
    }

    @Test
    void loginAndOut() throws Exception
    {
        mockMvc.perform(
                formLogin().user(testUsername).password(testPassword)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login/success"))
                .andExpect(authenticated().withUsername("admin"));

        mockMvc.perform(logout())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(unauthenticated());
    }
}