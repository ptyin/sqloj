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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

/***
 * Configuration related to security.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
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
        provider.setUserDetailsService(userDetailsService);
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
            http.csrf().disable()
                .httpBasic().disable()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl("/login/success")
                    .failureForwardUrl("/login/failure")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) ->
                    {
                        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                        httpServletResponse.setContentType("application/json");
                        httpServletResponse.getWriter().write("{\"success\" : true}");
                    })
            ;
        }
    }
}
