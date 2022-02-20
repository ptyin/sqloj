package asia.ptyin.sqloj.engine.judge;

import asia.ptyin.sqloj.engine.judge.comparator.*;
import asia.ptyin.sqloj.engine.judge.comparator.Comparator;
import asia.ptyin.sqloj.engine.sql.QueryResult;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;

/***
 * Multi-functional judge worker.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class JudgeWorker implements Callable<Map<String, Object>>
{
    private final List<? extends Comparator> comparators;
    private final QueryResult submit, criterion;

    public enum JudgeOption
    {
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
    private static final JudgeOption[] DEFAULT_OPTIONS = {JudgeOption.COLUMN_LABEL, JudgeOption.ROW};

    public JudgeWorker(QueryResult submit, QueryResult criterion)
    {
        this(submit, criterion, DEFAULT_OPTIONS);
    }

    public JudgeWorker(QueryResult submit, QueryResult criterion, JudgeOption[] options)
    {
        this.submit = submit;
        this.criterion = criterion;

        this.comparators = Arrays.stream(options)
                .map(option ->
                {
                    try
                    {
                        return option.getComparator().getDeclaredConstructor().newInstance();
                    } catch (Throwable e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }).toList();
    }

    @Override
    public Map<String, Object> call() throws Exception
    {
        var comments = new HashMap<String, Object>();
        var start = System.currentTimeMillis();
        for (var comparator : comparators)
            comparator.compare(submit, criterion, comments);
        comments.put("judge costs", Duration.ofMillis(System.currentTimeMillis() - start));
        return comments;
    }

}
