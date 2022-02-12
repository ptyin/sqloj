package asia.ptyin.sqloj.user.security;

import asia.ptyin.sqloj.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 * Use for adapting User to spring security UserDetails
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class UserDetailsAdapter implements UserDetails
{
    private final UserEntity user;
    private final List<GrantedAuthority> grantedAuthorityList;

    public UserDetailsAdapter(UserEntity user)
    {
        this.user = user;
        grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
    }

    public UserEntity getUser()
    {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return grantedAuthorityList;
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
