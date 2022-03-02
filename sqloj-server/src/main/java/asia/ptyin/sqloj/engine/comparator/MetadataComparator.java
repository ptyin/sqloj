package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.result.ExecutionResult;
import asia.ptyin.sqloj.engine.result.QueryResult;

import java.util.Map;

/***
 * Comparator which is sensitive to the metadata of result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class MetadataComparator implements LastQueryResultComparator
{
    @Override
    public boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments)
    {
        return false;
    }
}
