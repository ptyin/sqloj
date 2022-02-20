package asia.ptyin.sqloj.engine.judge;

import asia.ptyin.sqloj.engine.sql.QueryResult;

/***
 * Comparator compares two {@link QueryResult} and returns a {@link JudgeResult}.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface Comparator
{
    JudgeResult compare(QueryResult a, QueryResult b);
}
