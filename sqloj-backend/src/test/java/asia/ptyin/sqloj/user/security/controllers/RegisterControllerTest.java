package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.config.SqlOjConfigurationProperties;
import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.entities.User;
import asia.ptyin.sqloj.user.entities.UserDto;
import asia.ptyin.sqloj.user.security.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RegisterController.class)
class RegisterControllerTest
{
    @MockBean
    UserRepository repository;

    @TestConfiguration
    @Import(SqlOjConfigurationProperties.class)
    static class AdditionalConfiguration
    {
        @Autowired
        UserRepository repository;
        @Autowired
        SqlOjConfigurationProperties.Admin defaultAdminProperties;

        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityService securityService()
        {
            when(repository.findByUsername("admin")).thenReturn(User.createDefaultAdmin("admin", "admin@123"));
            return new SecurityService(repository, passwordEncoder(), defaultAdminProperties);
        }
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    SecurityService securityService;

    final String testUsername;
    final String testPassword;

    RegisterControllerTest()
    {
        this.testUsername = "register-test";
        this.testPassword = "test@123";
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "securityService")
    @Test
    void register() throws Exception
    {
        when(repository.existsByUsername(testUsername)).thenReturn(false);
        when(repository.save(any())).thenReturn(null);
        UserDto userDto = new UserDto(testUsername, testPassword);
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