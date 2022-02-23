package asia.ptyin.sqloj.engine;

import asia.ptyin.sqloj.engine.comparator.*;
import asia.ptyin.sqloj.engine.comparator.Comparator;
import asia.ptyin.sqloj.engine.result.ExecutionResult;
import asia.ptyin.sqloj.engine.result.JudgeResult;
import asia.ptyin.sqloj.engine.result.QueryResult;
import asia.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import asia.ptyin.sqloj.engine.task.Task;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

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
    private final Duration timeLimit;  // TODO add time limit support.

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

    public JudgeTask(UUID uuid, Connection connection, String sql, ExecutionResult criterion)
    {
        this(uuid, connection, sql, criterion, null, DEFAULT_OPTIONS);
    }

    public JudgeTask(UUID uuid, Connection connection, String sql, ExecutionResult criterion, @Nullable Duration timeLimit, JudgeOption[] options)
    {
        super(uuid);
        this.connection = connection;
        this.sql = sql;
        this.criterion = criterion;
        this.timeLimit = timeLimit;
        this.options = options;
    }

    @Override
    protected JudgeResult run() throws InterruptedException
    {
        long executionStart = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        QueryResult lastQueryResult = null;
        try
        {
            var executionResult = SqlExecutionUtils.execute(connection, sql);
            lastQueryResult = executionResult.getLastQueryResult();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }


        var result = new JudgeResult();
        var comments = result.getComments();
        boolean pass = false;
        for (var option : options)
        {
            if(Thread.currentThread().isInterrupted())
                throw new InterruptedException("Interruption before comparison option [%s]".formatted(option));
            try
            {
                var comparator = option.getComparator().getDeclaredConstructor().newInstance();
                pass = comparator.compare(lastQueryResult, criterion.getLastQueryResult(), comments);
                if(!pass)
                    break;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        result.setPass(pass);
        return result;
    }

}
