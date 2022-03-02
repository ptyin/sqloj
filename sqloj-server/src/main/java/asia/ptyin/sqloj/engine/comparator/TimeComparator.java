package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.result.ExecutionResult;
import asia.ptyin.sqloj.engine.result.QueryResult;

import java.time.Duration;
import java.util.Map;

public class TimeComparator implements Comparator
{
    @Override
    public boolean compare(ExecutionResult x, ExecutionResult criterion, Map<String, Object> comments)
    {
        var diff = criterion.getTime().minus(x.getTime());  // Pass if non-negative.
        if(diff.isNegative())
            comments.put("time executed", "submitted: %s, time limit: %s".formatted(x.getTime(), criterion.getTime()));
        return !diff.isNegative();
    }
}
