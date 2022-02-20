package asia.ptyin.sqloj.engine.task;


import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class Task<T> implements Callable<T>
{
    @Getter
    private final UUID uuid;
    /**
     * Elapsed thread CPU time of execution of the task.
     */
    @Getter
    private Duration elapsedTime = Duration.ZERO;

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

    public abstract T run() throws Exception;

    @Override
    public T call() throws Exception
    {
        taskState = TaskState.RUNNING;
        long start = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        var result = run();
        elapsedTime = Duration.ofNanos(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - start);
        taskState = TaskState.FINISHED;
        return result;
    }

}
