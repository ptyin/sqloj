package net.ptyin.sqloj.user.security.controllers;

import net.ptyin.sqloj.user.UserDto;
import net.ptyin.sqloj.user.UserEntity;
import net.ptyin.sqloj.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest
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
        when(repository.existsByUsername(testUsername)).thenReturn(false);
        when(repository.save(any())).thenReturn(null);
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    @Test
    void register() throws Exception
    {
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