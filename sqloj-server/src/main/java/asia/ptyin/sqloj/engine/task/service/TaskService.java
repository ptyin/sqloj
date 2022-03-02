package asia.ptyin.sqloj.engine.task.service;

import asia.ptyin.sqloj.engine.result.Result;
import asia.ptyin.sqloj.engine.task.TaskEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

/***
 * Service interface for Task.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface TaskService
{
    List<TaskEntity> getAllByUser(UUID userUuid);
    TaskEntity getTask(UUID uuid);
    void deleteTask(UUID uuid);

    static <T extends Result> T getResult(String resultJson, Class<T> resultClass)
    {
        T value = null;
        try
        {
            value = Result.objectMapper.readValue(resultJson, resultClass);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return value;
    }
}
