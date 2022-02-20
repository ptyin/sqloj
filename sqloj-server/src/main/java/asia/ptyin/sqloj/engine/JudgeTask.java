package asia.ptyin.sqloj.engine;

import asia.ptyin.sqloj.engine.comparator.*;
import asia.ptyin.sqloj.engine.comparator.Comparator;
import asia.ptyin.sqloj.engine.sql.QueryResult;
import asia.ptyin.sqloj.engine.task.Task;
import lombok.Getter;

import java.time.Duration;
import java.util.*;

/***
 * Multi-functional judge worker.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class JudgeTask extends Task<Map<String, Object>>
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

    public JudgeTask(UUID uuid, QueryResult submit, QueryResult criterion)
    {
        this(uuid, submit, criterion, DEFAULT_OPTIONS);
    }

    public JudgeTask(UUID uuid, QueryResult submit, QueryResult criterion, JudgeOption[] options)
    {
        super(uuid);
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
    public Map<String, Object> run() throws Exception
    {
        var comments = new HashMap<String, Object>();
        var start = System.currentTimeMillis();
        for (var comparator : comparators)
            comparator.compare(submit, criterion, comments);
        comments.put("judge costs", Duration.ofMillis(System.currentTimeMillis() - start));
        return comments;
    }

}
