package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.result.QueryResult;

import java.util.Map;

public class TimeComparator implements Comparator
{
    @Override
    public boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments)
    {
        return false;
    }
}
