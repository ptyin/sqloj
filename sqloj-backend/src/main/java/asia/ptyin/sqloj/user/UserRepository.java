package asia.ptyin.sqloj.user;

import asia.ptyin.sqloj.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/***
 * JPA repository interface for user.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface UserRepository extends JpaRepository<User, String>
{
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
