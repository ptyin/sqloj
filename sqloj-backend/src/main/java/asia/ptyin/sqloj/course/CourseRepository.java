package asia.ptyin.sqloj.course;

import asia.ptyin.sqloj.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID>
{
    List<CourseEntity> findAllByParticipatorList_Uuid(UUID userUuid);
}
