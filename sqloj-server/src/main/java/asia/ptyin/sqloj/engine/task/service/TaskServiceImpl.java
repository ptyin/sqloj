package asia.ptyin.sqloj.engine.task.service;

import asia.ptyin.sqloj.engine.task.TaskEntity;
import asia.ptyin.sqloj.engine.task.TaskRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/***
 * Default implementation for {@link TaskService}.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Service
public class TaskServiceImpl implements TaskService
{
    @Setter(onMethod_ = @Autowired)
    private TaskRepository repository;

    @Override
    public List<TaskEntity> getAllByUser(UUID userUuid)
    {
        return repository.findAllByCreatedBy(userUuid);
    }

    @Override
    public TaskEntity getTask(UUID uuid)
    {
        return repository.findById(uuid).orElse(null);
    }

    @Override
    public void deleteTask(UUID uuid)
    {
        repository.deleteById(uuid);
    }

}
