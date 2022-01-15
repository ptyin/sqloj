package asia.ptyin.sqloj.user.security.login;

import org.springframework.security.core.context.SecurityContextHolder;

public class LoginServiceImpl implements LoginService
{
    @Override
    public boolean login(String username, String password)
    {
        var context = SecurityContextHolder.createEmptyContext();
//        var authentication = new UsernamePasswordAuthenticationToken()
        return false;
    }
}
