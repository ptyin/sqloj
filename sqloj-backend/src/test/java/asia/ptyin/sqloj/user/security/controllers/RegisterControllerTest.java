package asia.ptyin.sqloj.user.security.controllers;

import asia.ptyin.sqloj.SqlOjApplication;
import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.entities.User;
import asia.ptyin.sqloj.user.entities.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;


@WebMvcTest
class RegisterControllerTest
{
    @MockBean
    UserRepository repository;

    @TestConfiguration
    static class AdditionalConfiguration
    {
        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    MockMvc mockMvc;

    final String testUsername;
    final String testPassword;

    RegisterControllerTest()
    {
        this.testUsername = "register-test";
        this.testPassword = "test@123";
    }

//    @AfterEach
//    void destroy()
//    {
//        var testUserEntity = repository.findByUsername(testUsername);
//        if(testUserEntity != null)
//            repository.delete(testUserEntity);
//    }

    @Test
    void register() throws Exception
    {
        when(repository.existsByUsername(testUsername)).thenReturn(false);
        when(repository.save(any())).thenReturn(null);
        UserDto userDto = new UserDto(testUsername, testPassword);
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(post("/register").content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}