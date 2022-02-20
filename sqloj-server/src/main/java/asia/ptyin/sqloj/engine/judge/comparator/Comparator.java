package asia.ptyin.sqloj.engine.judge.comparator;

import asia.ptyin.sqloj.engine.judge.JudgeResult;
import asia.ptyin.sqloj.engine.sql.QueryResult;

import java.util.HashMap;
import java.util.Map;

/***
 * Comparator compares two {@link QueryResult} and returns a {@link JudgeResult}.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface Comparator
{
    /**
     * Compares the query result with criterion and add some comments of that comparison.
     * @param x The one that is used to be compared with criterion.
     * @param criterion The criterion query result.
     * @param comments The result container, and several entries after each compare.
     * @return {@code true} if x equals to criterion, otherwise {@code false}.
     */
    boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments);
}
