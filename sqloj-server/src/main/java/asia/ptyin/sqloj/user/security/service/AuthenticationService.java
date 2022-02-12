package asia.ptyin.sqloj.user.security.service;

import asia.ptyin.sqloj.user.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthenticationService
{
    UserEntity getUser(Authentication authentication);
    UUID getUserUuid(Authentication authentication);
    String getUsername(Authentication authentication);
}
