package asia.ptyin.sqloj;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@Log4j2
@EnableSwagger2
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class SqlOjApplication
{
    @PostConstruct
    public void init()
    {
        log.info("欢迎使用SQL OpenJudge");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(SqlOjApplication.class, args);
    }

}
