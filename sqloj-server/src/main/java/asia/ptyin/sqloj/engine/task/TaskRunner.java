package asia.ptyin.sqloj.engine.task;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/***
 * Thread-safe {@link Task} runner.
 * It will terminate threads that run too long.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Component
public class TaskRunner<T>
{
    private Task<T> task;
    private T result;
    private Map<UUID, Task<?>> container;
    private final ExecutorService threadPool;

    public TaskRunner()
    {
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Submit a task to the cached thread pool and run the task until timeout.
     * If reaches timeout which means the thread runs too long, then terminates it.
     * @param task The task to submit.
     * @return the future result that the task produces.
     */
    public Future<?> submit(Task<?> task)
    {
        var future = threadPool.submit(task);
        threadPool.submit(() ->
        {
            try
            {
                future.get(30, TimeUnit.MINUTES);

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                e.printStackTrace();
            } catch (TimeoutException e)
            {
                e.printStackTrace();
            }
        });
        return null;
    }
}
