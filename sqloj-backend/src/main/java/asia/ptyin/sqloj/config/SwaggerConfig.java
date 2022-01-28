package asia.ptyin.sqloj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/***
 * Configuration class for Spring Fox Swagger 2.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Configuration
public class SwaggerConfig
{
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SQL OpenJudge Documentation")
                .description("A lightweight database system experimental platform that integrates an OJ of SQL and a variety of practical functionalities.")
                .contact(new Contact("PTYin", "https://github.com/PTYin", "ptyin@example.com"))
                .version("0.1.0")
                .build();
    }
}
