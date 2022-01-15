package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/***
 * Custom UserDetailService in order to provide UserDetail from data source.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Service
public class SecurityService implements UserDetailsService
{
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository)
    {
        this.repository = repository;
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
