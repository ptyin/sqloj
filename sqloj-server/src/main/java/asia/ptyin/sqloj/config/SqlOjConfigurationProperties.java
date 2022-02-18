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
        /**
         * Period between 2 consecutive database connection recycling task.
         * Default 1 minute.
         */
        private long connectionRecyclePeriod = 1000L * 60L;
        /**
         * Max live duration for an arbitrary database connection.
         * Default 1 hour.
         */
        private long connectionMaxLiveDuration = 1000L * 60L * 60L;

        private String mysqlDefaultUsername = "root";
        private String mysqlDefaultPassword = "mysql@sqloj";


        private String postgresqlDefaultUsername = "postgres";
        private String postgresqlDefaultPassword = "postgres@sqloj";

    }
}
