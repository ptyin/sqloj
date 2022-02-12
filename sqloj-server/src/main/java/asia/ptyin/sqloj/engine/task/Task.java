package asia.ptyin.sqloj.engine.task;


public abstract class Task implements Runnable
{
    TaskState state = TaskState.VIRGIN;
    /**
     * A lock object in order to control the access to the internal of task.
     */
    final Object lock = new Object();

    public abstract void run();

    public void terminate()
    {
        synchronized (lock)
        {
            state = TaskState.TERMINATED;
        }
    }

}
