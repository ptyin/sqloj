package net.ptyin.sqloj.engine.task.impl;

import lombok.Getter;
import net.ptyin.sqloj.engine.comparators.*;
import net.ptyin.sqloj.engine.result.ExecutionResult;
import net.ptyin.sqloj.engine.result.JudgeResult;
import net.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import net.ptyin.sqloj.engine.task.Task;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Map;

/***
 * Multi-functional judge worker.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class JudgeTask extends Task<JudgeResult>
{
//    private final List<? extends Comparator> comparators;
    private final Connection connection;
    private final String sql;
    private final ExecutionResult criterion;

    public enum JudgeOption
    {
        TIME_LIMIT(TimeComparator.class),
        COLUMN_LABEL(ColumnLabelComparator.class),
        ROW(RowComparator.class),
        METADATA(MetadataComparator.class),
        ORDER(OrderComparator.class);
        @Getter
        final Class<? extends Comparator> comparator;
        JudgeOption(Class<? extends Comparator> comparator)
        {
            this.comparator = comparator;
        }
    }
    private final JudgeOption[] options;

    private static final JudgeOption[] DEFAULT_OPTIONS = {JudgeOption.COLUMN_LABEL, JudgeOption.ROW};

    public JudgeTask(Connection connection, String sql, ExecutionResult criterion)
    {
        this(connection, sql, criterion, null, DEFAULT_OPTIONS);
    }

    public JudgeTask(Connection connection, String sql, ExecutionResult criterion, Duration timeLimit, JudgeOption[] options)
    {
        this.connection = connection;
        this.sql = sql;
        this.criterion = criterion;
        if (timeLimit != null)
            criterion.setTime(criterion.getTime().plus(timeLimit));
        this.options = options;
    }

    @Override
    protected JudgeResult invoke() throws InterruptedException
    {
        long executionStart = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        ExecutionResult executionResult = null;
        try
        {
            executionResult = SqlExecutionUtils.execute(connection, sql);
            Duration elapsedTime = Duration.ofNanos(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - executionStart);
            executionResult.setTime(elapsedTime);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }


        JudgeResult result = new JudgeResult();
        Map<String, Object> comments = result.getComments();
        boolean pass = false;
        for (JudgeOption option : options)
        {
            if(Thread.currentThread().isInterrupted())
                throw new InterruptedException(String.format("Interruption before comparison option [%s]", option));
            try
            {
                Comparator comparator = option.getComparator().getDeclaredConstructor().newInstance();
                pass = comparator.compare(executionResult, criterion, comments);
                if(!pass)
                    break;
            } catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        result.setPass(pass);
        return result;
    }

}
