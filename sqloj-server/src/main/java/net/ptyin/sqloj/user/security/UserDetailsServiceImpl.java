package net.ptyin.sqloj.user.security;

import net.ptyin.sqloj.config.SqlOjConfigurationProperties;
import net.ptyin.sqloj.user.UserEntity;
import net.ptyin.sqloj.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/***
 * Custom UserDetailsService in order to provide UserDetails from data source.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, SqlOjConfigurationProperties.Admin defaultAdminProperties)
    {
        this.repository = repository;
        var defaultAdmin = UserEntity.createDefaultAdmin(defaultAdminProperties.getUsername(), passwordEncoder.encode(defaultAdminProperties.getPassword()));
        log.info("The admin username is '%s' and default password is '%s'.".formatted(defaultAdmin.getUsername(), defaultAdmin.getPassword()));
        if(repository.findByUsername(defaultAdminProperties.getUsername()) == null)  // If admin already exists, then don't create.
            repository.save(defaultAdmin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = repository.findByUsername(username);
        if(user == null)
        {
            throw new UsernameNotFoundException("Username %s not found.".formatted(username));
        }

        return new UserDetailsAdapter(user);
    }
}
