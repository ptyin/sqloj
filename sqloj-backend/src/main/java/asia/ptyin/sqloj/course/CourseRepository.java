package asia.ptyin.sqloj.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID>
{
    List<CourseEntity> findAllByParticipatorList_Uuid(UUID userUuid);
//    Course findByParticipatorList_Username(String username);

}
