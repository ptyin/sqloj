package net.ptyin.sqloj.engine.task;

import lombok.Getter;
import lombok.Setter;
import net.ptyin.sqloj.engine.result.Result;

import java.util.concurrent.*;

/***
 * Thread-safe {@link Task} execution service.
 * It will terminate threads that run too long.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class TaskService
{
    /**
     * Consumer thread for dispatching tasks to thread pool,
     * and cancel the task until timeout.
     */
    protected class DispatcherThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                synchronized (TaskService.this)
                {
                    if (shutdown && reservations == 0)
                    {
                        workers.shutdown();
                    }
                }
                Task<Result> task = queue.take();
                synchronized (TaskService.this)
                {
                    --reservations;
                }
                Future<Result> future = workers.submit(task);
                try
                {
                    future.get(timeout, TimeUnit.SECONDS);
                } catch (TimeoutException | ExecutionException e)
                {
                    throw new TaskFailedException(e);
                } finally
                {
                    // If the task has been already done,
                    // the cancellation has no effect.
                    future.cancel(true);
                }
            } catch (InterruptedException e)
            {
                throw new TaskServiceShutdownException("TaskRunner is being shut down by interruption.");
            }
        }
    }

    @Setter
    private long timeout;
    private final int nDispatcherThreads;
    @Getter
    private boolean shutdown = false;
    private int reservations = 0;
    protected final BlockingQueue<Task<Result>> queue = new LinkedBlockingQueue<>();
    protected final ExecutorService dispatchers;
    protected final ExecutorService workers;

    /**
     * Create a {@link TaskService} with 30 minutes timeout and 20 dispatchers.
     */
    public TaskService()
    {
        this.timeout = 30 * 60;
        this.nDispatcherThreads = 20;
        this.dispatchers = Executors.newFixedThreadPool(20);
        this.workers = Executors.newCachedThreadPool();
        init();
    }

    /**
     * Create a {@link TaskService} with specified timeout and number of dispatcher threads.
     * Note that the newly created threads are more than the parameter {@code nThreads}.
     * The {@code nThreads} merely specifies the threads of dispatchers,
     * and there are worker threads maintained by cached thread pool as well.
     * @param timeout Waiting timeout in <strong>SECOND</strong> unit.
     * @param nDispatcherThreads The number of dispatcher threads in thread pool.
     */
    public TaskService(long timeout, int nDispatcherThreads)
    {
        this.timeout = timeout;
        this.nDispatcherThreads = nDispatcherThreads;
        this.dispatchers = Executors.newFixedThreadPool(nDispatcherThreads);
        this.workers = Executors.newCachedThreadPool();
        init();
    }

    private void init()
    {
        for (int i = 0; i < nDispatcherThreads; i++)
        {
            dispatchers.submit(new DispatcherThread());
        }
    }

    /**
     * Submit a task to the cached thread pool and execute the task until timeout.
     * If reaches timeout which means the thread runs too long, then terminates it.
     * @param task The task to submit.
     * @throws TaskServiceShutdownException If TaskRunner is shut down.
     */
    public void submit(Task<Result> task) throws IllegalStateException
    {
        try
        {
            synchronized (this)
            {
                if (!shutdown)
                    throw new TaskServiceShutdownException("TaskRunner has already been shut down.");
                reservations++;
            }
            queue.put(task);
        } catch (InterruptedException e)
        {
            throw new TaskServiceShutdownException("TaskRunner is being shut down by interruption.");
        }
    }

    /**
     * Shutdown the runner and stop receiving new tasks.
     * But it still executes the undone tasks.
     */
    public void shutdown()
    {
        synchronized (this)
        {
            shutdown = true;
        }
        dispatchers.shutdown();
    }

    public void shutdownNow()
    {
        dispatchers.shutdownNow();
    }
}
