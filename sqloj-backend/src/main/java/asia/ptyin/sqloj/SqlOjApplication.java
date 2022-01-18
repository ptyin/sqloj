package asia.ptyin.sqloj;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Log4j2
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
