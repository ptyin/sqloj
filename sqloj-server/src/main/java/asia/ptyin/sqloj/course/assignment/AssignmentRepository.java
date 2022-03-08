package asia.ptyin.sqloj.course.assignment;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, UUID>
{
    List<AssignmentEntity> findAllByCourse_Uuid(UUID courseUuid);
}
