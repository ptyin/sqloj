package net.ptyin.sqloj.engine.task;

/***
 * Throw if a task failed in any reason.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class TaskFailedException extends RuntimeException
{
    /**
     * Constructs an {@link TaskFailedException} with the specified detail
     * message and cause.
     *
     * @param  message the detail message
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public TaskFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@link TaskFailedException} with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of {@code cause}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public TaskFailedException(Throwable cause) {
        super(cause);
    }
}
