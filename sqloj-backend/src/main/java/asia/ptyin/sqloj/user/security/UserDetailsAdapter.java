package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/***
 * Use to adapt spring security UserDetails
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public record UserDetailsAdapter(User user) implements UserDetails
{

    public User getUser()
    {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return user.isEnabled();
    }
}
