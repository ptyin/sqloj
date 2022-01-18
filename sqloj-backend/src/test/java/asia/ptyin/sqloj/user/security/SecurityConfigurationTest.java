package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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

    public SecurityConfigurationTest()
    {
        super("admin", "admin@123");
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