package asia.ptyin.sqloj.user.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InappropriatePrincipalException extends AuthenticationException
{
    public InappropriatePrincipalException(String msg)
    {
        super(msg);
    }

    public InappropriatePrincipalException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
