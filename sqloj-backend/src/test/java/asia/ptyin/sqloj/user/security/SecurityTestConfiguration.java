package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.config.SqlOjConfigurationProperties;
import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.when;

@TestConfiguration
@Import(SqlOjConfigurationProperties.class)
public class SecurityTestConfiguration
{
    @MockBean
    UserRepository repository;
    @Autowired
    SqlOjConfigurationProperties.Admin defaultAdminProperties;

    @PostConstruct
    public void init()
    {
        when(repository.findByUsername("admin")).thenReturn(User.createDefaultAdmin("admin", passwordEncoder().encode("admin@123")));
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityService securityService()
    {
        return new SecurityService(repository, passwordEncoder(), defaultAdminProperties);
    }
}
