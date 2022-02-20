package asia.ptyin.sqloj.engine.task;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * Thread-safe {@link Task} runner.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Component
public class TaskRunner<T>
{
    private Task<T> task;
    private T result;
    private final ExecutorService threadPool;

    public TaskRunner()
    {
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void run()
    {
        threadPool.submit(task);
    }
}
