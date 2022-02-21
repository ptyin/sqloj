package asia.ptyin.sqloj.engine.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/***
 * JPA repository of {@link Task}.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface TaskRepository extends JpaRepository<TaskEntity, UUID>
{

}
