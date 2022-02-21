package asia.ptyin.sqloj.engine.task;


import asia.ptyin.sqloj.engine.result.Result;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;

@Log4j2
public abstract class Task<T extends Result> implements Callable<T>
{
    @Getter
    private final UUID uuid;
    /**
     * Elapsed thread CPU time of execution of the task.
     */
    private Duration elapsedTime = Duration.ZERO;

    private T result;

    public enum TaskState
    {
        PENDING,
        RUNNING,
        FINISHED;
    }

    @Getter
    private TaskState taskState = TaskState.PENDING;

    public Task(UUID uuid)
    {
        this.uuid = uuid;
    }

    public abstract T run() throws InterruptedException;

    @Override
    public T call() throws Exception
    {
        T result = null;
        try
        {
            taskState = TaskState.RUNNING;  // Reference assignment is atomic.
            long start = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            result = run();  // Throw InterruptedException.
            elapsedTime = Duration.ofNanos(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - start);
            taskState = TaskState.FINISHED;
        } catch (InterruptedException e)
        {
            log.error("Task [%s] is terminated due to wait timeout.".formatted(uuid));
            e.printStackTrace();
        } catch (Throwable e)
        {
            e.printStackTrace();
        }

        return result;
    }

}
