package net.ptyin.sqloj.engine.comparators;

import net.ptyin.sqloj.engine.result.ExecutionResult;

import java.time.Duration;
import java.util.Map;

public class TimeComparator implements Comparator
{
    @Override
    public boolean compare(ExecutionResult x, ExecutionResult criterion, Map<String, Object> comments)
    {
        Duration diff = criterion.getTime().minus(x.getTime());  // Pass if non-negative.
        if(diff.isNegative())
            comments.put("time executed",
                    String.format("submitted: %s, time limit: %s", x.getTime(), criterion.getTime()));
        return !diff.isNegative();
    }
}
