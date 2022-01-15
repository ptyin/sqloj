package asia.ptyin.sqloj.user.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
    private SecurityService securityService;

    @Autowired
    public void setSecurityService(SecurityService securityService)
    {
        this.securityService = securityService;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        var provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsPasswordService(userDetailsService());
        provider.setUserDetailsService(securityService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Configuration
    public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
    {
        /**
         * But when you use WebSecurity and use ignoring() method,
         * any requests to resources will completely bypass the Spring Security Filter Chain all together.
         * @param web Web security chain.
         * @throws Exception Exception.
         */
        @Override
        public void configure(WebSecurity web) throws Exception
        {
            web.ignoring().antMatchers("/public/**");
        }

        /**
         * When you use HttpSecurity and try to permitAll() requests.
         * Your requests will be allowed to be accessed from the Spring Security Filter Chain.
         * @param http HTTP security chain.
         * @throws Exception Exception.
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            log.debug("Using user defined configure(HttpSecurity).");
            http.authorizeRequests((requests) -> ((AuthorizedUrl)requests.anyRequest()).authenticated())
                    .formLogin().disable()
                    .httpBasic().disable();
        }
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("test")
                .password("test@123")
                .roles("TEACHER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
