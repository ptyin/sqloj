package asia.ptyin.sqloj.user;

import asia.ptyin.sqloj.course.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/***
 * JPA repository interface for user.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID>
{
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
    List<UserEntity> findAllByParticipatedCourseList_Uuid(UUID courseUuid);
}
