package asia.ptyin.sqloj.engine.task;


import asia.ptyin.sqloj.engine.result.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;

@Log4j2
public abstract class Task<T extends Result> implements Callable<T>
{
    /**
     * UUID can be retrieved if and only if the task has been submitted.
     */
    @Getter @Setter
    private UUID uuid;

    public enum TaskState
    {
        PENDING,
        RUNNING,
        FINISHED;
    }

    @Getter
    private TaskState taskState = TaskState.PENDING;

    /**
     * Run the task, no need to record the duration.
     * @return The task result.
     * @throws InterruptedException Throw if the task is cancelled.
     */
    protected abstract T run() throws InterruptedException;

    @Override
    public T call() throws Exception
    {
        T result = null;
        try
        {
            taskState = TaskState.RUNNING;  // Reference assignment is atomic.
            long start = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            result = run();  // Throw InterruptedException.
            Duration elapsedTime = Duration.ofNanos(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - start);
            result.setTime(elapsedTime);
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
