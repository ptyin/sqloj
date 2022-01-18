package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.entities.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(SecurityTestConfiguration.class)
class SecurityConfigurationTest extends SecurityTestBase
{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository repository;


    @Autowired
    public SecurityConfigurationTest(PasswordEncoder passwordEncoder)
    {
        super("admin", "admin@123");
    }

    @Test
    void login() throws Exception
    {
        mockMvc.perform(
                formLogin().user(testUsername).password(testPassword)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login/success"))
                .andExpect(authenticated().withUsername("admin"))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.role").value("TEACHER"))
        ;
    }
}