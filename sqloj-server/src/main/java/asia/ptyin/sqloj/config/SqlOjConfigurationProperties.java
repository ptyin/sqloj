package asia.ptyin.sqloj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SqlOjConfigurationProperties
{
    @ConfigurationProperties(prefix = "sqloj.user.admin")
    @Configuration
    @Data
    public static class Admin
    {

        private String username;
        private String password;
    }

    @ConfigurationProperties(prefix = "sqloj.engine")
    @Configuration
    @Data
    public static class Engine
    {
        /**
         * Max size of threads in thread pool.
         */
        private int maxThreads;

        private String urlToDb = ":resource:db/";
    }
}
