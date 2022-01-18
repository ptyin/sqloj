package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.config.SqlOjConfigurationProperties;
import asia.ptyin.sqloj.user.UserRepository;
import asia.ptyin.sqloj.user.entities.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/***
 * Custom UserDetailService in order to provide UserDetail from data source.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@Service
public class SecurityService implements UserDetailsService
{
    private final UserRepository repository;

    @Autowired
    public SecurityService(UserRepository repository, PasswordEncoder passwordEncoder, SqlOjConfigurationProperties.Admin defaultAdminProperties)
    {
        this.repository = repository;
        var defaultAdmin = User.createDefaultAdmin(defaultAdminProperties.getUsername(), passwordEncoder.encode(defaultAdminProperties.getPassword()));
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
