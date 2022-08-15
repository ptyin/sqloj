package net.ptyin.sqloj.engine.comparators;

import net.ptyin.sqloj.engine.result.ExecutionResult;
import net.ptyin.sqloj.engine.result.QueryResult;

import java.util.Map;

/***
 * Abstract class for Comparator of comparing just last query result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface LastQueryResultComparator extends Comparator
{
    boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments);

    default boolean compare(ExecutionResult x, ExecutionResult criterion, Map<String, Object> comments)
    {
        return compare(x.getLastQueryResult(), criterion.getLastQueryResult(), comments);
    }
}
