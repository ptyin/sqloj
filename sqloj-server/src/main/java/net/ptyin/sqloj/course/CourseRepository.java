package net.ptyin.sqloj.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID>
{
    List<CourseEntity> findAllByParticipatorList_Uuid(UUID userUuid);
    @Query("SELECT c FROM CourseEntity c JOIN FETCH c.participatorList WHERE c.uuid = (:uuid)")
    CourseEntity findByUuidAndFetchEagerly(@Param("uuid") UUID uuid);
}
