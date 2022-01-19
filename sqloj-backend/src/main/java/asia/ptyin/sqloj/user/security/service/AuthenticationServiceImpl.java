package asia.ptyin.sqloj.user.security.service;

import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.security.UserDetailsAdapter;
import asia.ptyin.sqloj.user.security.InappropriatePrincipalException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
    @Override
    public UserEntity getUser(Authentication authentication)
    {
        if(! (authentication.getPrincipal() instanceof UserDetailsAdapter))
            throw new InappropriatePrincipalException("Cannot cast principal to %s".formatted(UserDetailsAdapter.class));
        return ((UserDetailsAdapter) authentication.getPrincipal()).getUser();
    }

    @Override
    public UUID getUserUuid(Authentication authentication)
    {
        return getUser(authentication).getUuid();
    }

    @Override
    public String getUsername(Authentication authentication)
    {
        if(! (authentication.getPrincipal() instanceof UserDetails))
            throw new InappropriatePrincipalException("Cannot cast principal to %s".formatted(UserDetails.class));
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }
}
