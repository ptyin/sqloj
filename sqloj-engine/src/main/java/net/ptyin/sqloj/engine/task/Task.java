package net.ptyin.sqloj.engine.task;


import net.ptyin.sqloj.engine.result.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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

    @Getter
    private T result;

    private PropertyChangeSupport support;

    public enum TaskState
    {
        PENDING,
        RUNNING,
        FINISHED;
    }

    @Getter
    private TaskState state = TaskState.PENDING;

    /**
     * Run the task, no need to record the duration.
     * @return The task result.
     * @throws InterruptedException Throw if the task encounters cancellation.
     */
    protected abstract T invoke() throws InterruptedException;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * The wrapper method for {@code invoke()}, counts the elapsed time and fires property change events.
     * {@link PropertyChangeListener} will receive events twice during call.<br/>
     * <ul>
     *     <li>
     *         The first event is fired before the task property {@code state} change
     *         from {@code PENDING} to {@code RUNNING}.
     *     </li>
     *     <li>
     *         The second event is fired before the task property {@code state} change
     *         from {@code RUNNING} to {@code FINISHED}.
     *     </li>
     * </ul>
     * Once the task property {@code state} change to {@code FINISHED},
     * you can call <strong>{@code getResult()}</strong> to obtain the result of task.
     * @return The result of the task.
     * @throws InterruptedException If task has been interrupted.
     */
    @Override
    public T call() throws InterruptedException
    {
        try
        {
            support.firePropertyChange("state", state, TaskState.RUNNING);
            state = TaskState.RUNNING;  // Reference assignment is atomic.
            long start = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            result = invoke();  // Throw InterruptedException.
            Duration elapsedTime = Duration.ofNanos(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - start);
            result.setTime(elapsedTime);
            return result;
        } finally
        {
            support.firePropertyChange("state", state, TaskState.FINISHED);
            state = TaskState.FINISHED;
        }
    }

}
