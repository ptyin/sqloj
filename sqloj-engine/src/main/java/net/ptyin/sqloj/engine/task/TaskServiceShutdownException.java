package net.ptyin.sqloj.engine.task;

/***
 * Throw if {@link TaskService} has been shut down.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class TaskServiceShutdownException extends RuntimeException
{
    public TaskServiceShutdownException(String message)
    {
        super(message);
    }
}
