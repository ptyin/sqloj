package net.ptyin.sqloj.engine.comparators;

import net.ptyin.sqloj.engine.result.ExecutionResult;

import java.util.Map;

/***
 * Comparator compares two {@link ExecutionResult} to get comparison result and comments.
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
    boolean compare(ExecutionResult x, ExecutionResult criterion, Map<String, Object> comments);
}
