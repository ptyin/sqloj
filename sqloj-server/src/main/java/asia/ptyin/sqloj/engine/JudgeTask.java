package asia.ptyin.sqloj.engine;

import asia.ptyin.sqloj.engine.comparator.*;
import asia.ptyin.sqloj.engine.comparator.Comparator;
import asia.ptyin.sqloj.engine.result.JudgeResult;
import asia.ptyin.sqloj.engine.result.QueryResult;
import asia.ptyin.sqloj.engine.task.Task;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
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
    private final QueryResult submit, criterion;
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

    public JudgeTask(UUID uuid, QueryResult submit, QueryResult criterion)
    {
        this(uuid, submit, criterion, null, DEFAULT_OPTIONS);
    }

    public JudgeTask(UUID uuid, QueryResult submit, QueryResult criterion, @Nullable Duration timeLimit, JudgeOption[] options)
    {
        super(uuid);
        this.submit = submit;
        this.criterion = criterion;
        this.timeLimit = timeLimit;
        this.options = options;
    }

    @Override
    public JudgeResult run() throws InterruptedException
    {
        var result = new JudgeResult();
        var comments = result.getComments();
        var start = System.currentTimeMillis();
        boolean pass = false;
        for (var option : options)
        {
            if(Thread.currentThread().isInterrupted())
                throw new InterruptedException("Interruption before comparison option [%s]".formatted(option));
            try
            {
                var comparator = option.getComparator().getDeclaredConstructor().newInstance();
                pass = comparator.compare(submit, criterion, comments);
                if(!pass)
                    break;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        result.setPass(pass);
        result.setTime(Duration.ofMillis(System.currentTimeMillis() - start));
        return result;
    }

}
