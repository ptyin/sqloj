package asia.ptyin.sqloj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "sqloj")
@Configuration
@Data
public class SqlOjConfigurationProperties
{
    @ConfigurationProperties(prefix = "sqloj.admin")
    @Configuration
    @Data
    public static class Admin
    {

        private String username;
        private String password;
    }
}
