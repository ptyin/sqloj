package asia.ptyin.sqloj.engine.task;

import asia.ptyin.sqloj.engine.result.Result;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TaskRunner
{
    private Map<UUID, Task<?>> container;
    @Setter(onMethod_ = @Autowired)
    private TaskRepository repository;
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
    public <T extends Result> Future<T> submit(Task<T> task)
    {
        var future = threadPool.submit(task);
        threadPool.submit(() ->
        {
            try
            {
                T result = future.get(30, TimeUnit.MINUTES);  // TODO make timeout a property


            } catch (TimeoutException e)  // If the waited time exceeds timeout.
            {
                future.cancel(true);
                System.out.println();
                e.printStackTrace();
            } catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        });
        return null;
    }
}
